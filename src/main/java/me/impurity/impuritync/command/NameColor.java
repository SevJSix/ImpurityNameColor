package me.impurity.impuritync.command;

import me.impurity.impuritync.ImpurityNC;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class NameColor implements CommandExecutor, Listener {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("ImpurityNC.use")) {
                if (args.length > 0) {
                    if (args[0].equalsIgnoreCase("random")) {

                        /*
                        Arguments "random" and "rainbow" are inspired from an open source
                        name color plugin, "LeeesNC."
                        You can view LeeesNC at https://github.com/XeraPlugins/LeeesNC
                        */

                        List<ChatColor> colors = Arrays.asList(ChatColor.values());

                        StringBuilder randomBuilder = new StringBuilder();
                        List<Character> chars = new ArrayList<>();

                        for (int i = 0; i < player.getName().length(); i++) {
                            char c = player.getName().charAt(i);
                            chars.add(c);
                        }
                        int i = 0;
                        for (Character letter : chars) {
                            if (i == colors.size()) {
                                i = 0;
                            }
                            int index = ThreadLocalRandom.current().nextInt(colors.size());
                            randomBuilder.append(colors.get(index)).append(letter);
                            i++;
                        }
                        player.setDisplayName(randomBuilder.toString().replace("§k", "") + ChatColor.RESET);
                        ImpurityNC.sendMessage(player, "&6Your name is now: &r" + randomBuilder.toString().replace("§k", ""));
                        ImpurityNC.getPlugin().getPlayers().set(String.valueOf(player.getUniqueId()), randomBuilder.toString().replace("§k", ""));
                        ImpurityNC.getPlugin().saveConfig();
                        ImpurityNC.getPlugin().reloadPlayers();
                    } else {
                        if (args[0].equalsIgnoreCase("rainbow")) {
                            List<ChatColor> colors = Arrays.asList(ChatColor.DARK_RED, ChatColor.GOLD, ChatColor.YELLOW, ChatColor.DARK_GREEN, ChatColor.BLUE, ChatColor.DARK_BLUE, ChatColor.DARK_PURPLE);

                            StringBuilder rainbowBuilder = new StringBuilder();
                            List<Character> chars = new ArrayList<>();

                            for (int i = 0; i < player.getName().length(); i++) {
                                char c = player.getName().charAt(i);
                                chars.add(c);
                            }
                            int i = 0;
                            for (Character letter : chars) {
                                if (i == colors.size()) {
                                    i = 0;
                                }
                                rainbowBuilder.append(colors.get(i)).append(letter);
                                i++;
                            }
                            player.setDisplayName(rainbowBuilder.toString() + ChatColor.RESET);
                            ImpurityNC.sendMessage(player, "&6Your name is now: &r" + rainbowBuilder.toString());
                            ImpurityNC.getPlugin().getPlayers().set(String.valueOf(player.getUniqueId()), rainbowBuilder.toString().replace("§k", ""));
                            ImpurityNC.getPlugin().saveConfig();
                            ImpurityNC.getPlugin().reloadPlayers();
                        } else {

                            /*
                            Continue code if aguments are not "rainbow" or "random"
                            Instead of making a plethora of switch case statements,
                            we loop through args and define variable c as the color
                            value of args.
                            */

                            try {
                                StringBuilder builder = new StringBuilder();
                                for (String arg : args) {
                                    ChatColor c = ChatColor.valueOf(arg.toUpperCase().replace("magic", ""));
                                    builder.append(c);
                                }
                                player.setDisplayName(builder.toString().replace("§k", "") + player.getName() + ChatColor.RESET);
                                ImpurityNC.getPlugin().getPlayers().set(String.valueOf(player.getUniqueId()), builder.toString().replace("§k", "") + player.getName());
                                ImpurityNC.getPlugin().saveConfig();
                                ImpurityNC.getPlugin().reloadPlayers();
                                ImpurityNC.sendMessage(player, "&6Your name is now: &r" + builder.toString().replace("§k", "") + player.getName());
                            } catch (IllegalArgumentException ignored) {
                                ImpurityNC.sendMessage(player, "&4Invalid color type!");
                                ImpurityNC.sendMessage(player, "&6Available formats:" + "\n" + available());
                            }
                        }
                    }
                } else {
                    ImpurityNC.sendMessage(player, "&6Available colors:" + "\n" + available());
                }
            } else {
                ImpurityNC.sendMessage(player, "&6Sorry, you must donate to use name color.");
            }
        }
        return true;
    }
    
    // Join event things. Currently not cancellable.

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (ImpurityNC.getPlugin().getPlayers().contains(String.valueOf(player.getUniqueId()))) {
            if (player.hasPermission("ImpurityNC.use")) {
                player.setDisplayName(ImpurityNC.getPlugin().getPlayers().getString(String.valueOf(player.getUniqueId())) + ChatColor.RESET);
            } else {
                player.setDisplayName(player.getName());
            }
        }
        event.setJoinMessage(ChatColor.GRAY + player.getDisplayName() + ChatColor.GRAY + " joined");
    }

    private String available() {
        return ChatColor.translateAlternateColorCodes('&', "&4dark_red &cred &6gold &eyellow &2dark_green &agreen &baqua &3dark_aqua &1dark_blue &9blue &dlight_purple &5dark_purple &7gray &8dark_gray &0black&r &4r&6a&ei&2n&9b&1o&5w&r &lbold&r &mstrikethrough&r &nunderline&r &oitalic&r random reset");
    }
}
