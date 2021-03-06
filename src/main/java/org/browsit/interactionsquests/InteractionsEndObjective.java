/*
 * Copyright (c) 2021 Browsit, LLC. All rights reserved.
 *
 * THIS SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN
 * NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
 * OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.browsit.interactionsquests;

import java.util.AbstractMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import it.ajneb97.api.ConversationEndEvent;
import it.ajneb97.api.InteractionsConversation;
import me.blackvein.quests.CustomObjective;
import me.blackvein.quests.Quest;
import me.blackvein.quests.Quester;

public class InteractionsEndObjective extends CustomObjective implements Listener {

    public InteractionsEndObjective() {
        setName("Interactions End Objective");
        setAuthor("Browsit, LLC");
        setItem("PAPER", (short)0);
        setShowCount(true);
        addStringPrompt("Interactions End Obj", "Set a name for the objective", "End conversation");
        addStringPrompt("Interactions Conversation Name", "Enter conversation name", "ANY");
        setCountPrompt("Set the number of times to end the conversation");
        setDisplay("%Interactions End Obj%: %count%");
    }

    @Override
    public String getModuleName() {
        return "Interactions Quests Module";
    }

    @Override
    public Map.Entry<String, Short> getModuleItem() {
        return new AbstractMap.SimpleEntry<>("PAPER", (short)0);
    }
    
    @EventHandler
    public void conversationEnd(final ConversationEndEvent event) {
        final Player starter = event.getPlayer();
        final Quester quester = InteractionsModule.getQuests().getQuester(starter.getUniqueId());
        if (quester == null) {
            return;
        }
        for (final Quest q : quester.getCurrentQuests().keySet()) {
            final Map<String, Object> dataMap = getDataForPlayer(starter, this, q);
            if (dataMap != null) {
                final String convName = (String)dataMap.getOrDefault("Interactions Conversation Name", "ANY");
                if (convName == null) {
                    return;
                }
                final InteractionsConversation conv = event.getConversation();
                if (convName.equals("ANY") || convName.equalsIgnoreCase(conv.getName())) {
                    incrementObjective(starter, this, 1, q);
                    return;
                }
                return;
            }
        }
    }
}
