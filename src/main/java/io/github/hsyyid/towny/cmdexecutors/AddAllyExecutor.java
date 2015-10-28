package io.github.hsyyid.towny.cmdexecutors;

import io.github.hsyyid.towny.utils.ConfigManager;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.source.CommandBlockSource;
import org.spongepowered.api.util.command.source.ConsoleSource;
import org.spongepowered.api.util.command.spec.CommandExecutor;

import java.util.ArrayList;

public class AddAllyExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		String townName = ctx.<String> getOne("town name").get();

		if (src instanceof Player)
		{
			Player player = (Player) src;

			if (ConfigManager.getTeams().contains(townName))
			{
				String playerTeamName = null;
				boolean playerIsAMember = false;

				for (String team : ConfigManager.getTeams())
				{
					ArrayList<String> uuids = ConfigManager.getMembers(team);
					if (uuids.contains(player.getUniqueId().toString()))
					{
						playerIsAMember = true;
						break;
					}
					else if (ConfigManager.getLeader(team).equals(player.getUniqueId().toString()))
					{
						playerTeamName = team;
						break;
					}
				}

				if (playerTeamName != null)
				{
					try
					{
						if (ConfigManager.getAllies(playerTeamName) != null && ConfigManager.getAllies(playerTeamName).contains(townName))
						{
							player.sendMessage(Texts.of(TextColors.GREEN, "[Towny]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "You are already allies!"));
							return CommandResult.success();
						}
					}
					catch (NullPointerException e)
					{

					}
					try
					{
						if (ConfigManager.getEnemies(playerTeamName) != null && ConfigManager.getEnemies(playerTeamName).contains(townName))
						{
							ConfigManager.removeEnemy(playerTeamName, townName);
							ConfigManager.removeEnemy(townName, playerTeamName);
							ConfigManager.addAlly(playerTeamName, townName, true);
							player.sendMessage(Texts.of(TextColors.GREEN, "[Towny]: ", TextColors.GOLD, "Successfully set town ", TextColors.GREEN, townName, TextColors.GOLD, " as an ally!"));
							return CommandResult.success();
						}
					}
					catch (NullPointerException e)
					{
						;
					}

					ConfigManager.addAlly(playerTeamName, townName, true);
					player.sendMessage(Texts.of(TextColors.GREEN, "[Towny]: ", TextColors.GOLD, "Successfully set town ", TextColors.GREEN, townName, TextColors.GOLD, " as an ally!"));
				}
				else if (playerIsAMember)
				{
					player.sendMessage(Texts.of(TextColors.GREEN, "[Towny]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "Ask your leader to set allies!"));
				}
				else
				{
					player.sendMessage(Texts.of(TextColors.GREEN, "[Towny]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "You're not part of a town!"));
				}
			}
			else
			{
				player.sendMessage(Texts.of(TextColors.GREEN, "[Towny]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "Town does not exist!"));
			}
		}
		else if (src instanceof ConsoleSource)
		{
			src.sendMessage(Texts.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "Must be an in-game player to use /addally!"));
		}
		else if (src instanceof CommandBlockSource)
		{
			src.sendMessage(Texts.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "Must be an in-game player to use /addally!"));
		}

		return CommandResult.success();
	}
}
