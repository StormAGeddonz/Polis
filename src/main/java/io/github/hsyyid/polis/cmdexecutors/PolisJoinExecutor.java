package io.github.hsyyid.polis.cmdexecutors;

import io.github.hsyyid.polis.Polis;
import io.github.hsyyid.polis.utils.ConfigManager;
import io.github.hsyyid.polis.utils.Invite;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.source.CommandBlockSource;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class PolisJoinExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		String townName = ctx.<String> getOne("town name").get();

		if (src instanceof Player)
		{
			Player player = (Player) src;

			if (ConfigManager.getTeams().contains(townName))
			{
				for (Object t : ConfigManager.getTeams())
				{
					String team = String.valueOf(t);

					if (ConfigManager.getMembers(team).contains(player.getUniqueId().toString()))
					{
						src.sendMessage(Text.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "You are in a town, first leave that team by doing /polis leave!"));
						return CommandResult.success();
					}
					if (ConfigManager.getExecutives(team).contains(player.getUniqueId().toString()))
					{
						src.sendMessage(Text.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "You are in a town, first leave that team by doing /polis leave!"));
						return CommandResult.success();
					}
					else if (ConfigManager.getLeader(team).equals(player.getUniqueId().toString()))
					{
						src.sendMessage(Text.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "You are the leader of a town set a different leader using /polis setleader!"));
						return CommandResult.success();
					}
				}

				try
				{
					Invite inv = null;

					for (Invite invite : Polis.invites)
					{
						if (invite.getPlayerUUID().equals(player.getUniqueId().toString()))
						{
							inv = invite;
						}
					}

					if (inv != null)
					{
						ConfigManager.addTeamMember(townName, player.getUniqueId().toString());

						for (Player p : Sponge.getServer().getOnlinePlayers())
						{
							if ((p.getUniqueId().toString()).equals(ConfigManager.getLeader(townName)))
							{
								p.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.YELLOW, player.getName() + " has joined your Town!"));
							}
						}

						player.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.YELLOW, "Joined town " + townName));
						Polis.invites.remove(inv);
					}
					else
					{
						player.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "You don't have an invite to join that town!"));
					}
				}
				catch (NullPointerException e)
				{
					player.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "Failed to add you to town!"));
				}
			}
			else
			{
				player.sendMessage(Text.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "Town does not exist!"));
			}
		}
		else if (src instanceof ConsoleSource)
		{
			src.sendMessage(Text.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "Must be an in-game player to use /jointown!"));
		}
		else if (src instanceof CommandBlockSource)
		{
			src.sendMessage(Text.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "Must be an in-game player to use /jointown!"));
		}

		return CommandResult.success();
	}
}
