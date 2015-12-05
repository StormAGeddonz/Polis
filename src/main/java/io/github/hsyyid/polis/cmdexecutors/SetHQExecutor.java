package io.github.hsyyid.polis.cmdexecutors;

import io.github.hsyyid.polis.utils.ConfigManager;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.source.CommandBlockSource;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;

public class SetHQExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		if (src instanceof Player)
		{
			Player player = (Player) src;

			boolean isAMember = false;
			String teamName = null;
			
			for (String team : ConfigManager.getTeams())
			{
				if (ConfigManager.getLeader(team).equals(player.getUniqueId().toString()))
				{
					teamName = team;
					break;
				}
				else if (ConfigManager.getMembers(team).contains(player.getUniqueId().toString()))
				{
					isAMember = true;
					break;
				}
			}
			
			if (teamName != null)
			{
				ConfigManager.setHQ(teamName, player.getLocation(), player.getWorld().getName());
				src.sendMessage(Texts.of(TextColors.GREEN, "Success: ", TextColors.YELLOW, "HQ set."));
			}
			else if (isAMember)
			{
				src.sendMessage(Texts.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "You are not a member! Ask your leader to set the HQ!"));
			}
			else
			{
				src.sendMessage(Texts.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "You are not in a town!"));
			}
		}
		else if (src instanceof ConsoleSource)
		{
			src.sendMessage(Texts.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "Must be an in-game player to use /sethq!"));
		}
		else if (src instanceof CommandBlockSource)
		{
			src.sendMessage(Texts.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "Must be an in-game player to use /sethq!"));
		}

		return CommandResult.success();
	}
}
