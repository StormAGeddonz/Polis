package io.github.hsyyid.polis.cmdexecutors;

import io.github.hsyyid.polis.Polis;
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

public class AutoClaimExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		if (src instanceof Player)
		{
			Player player = (Player) src;
			
			String playerTeamName = null;
			boolean playerIsAMember = false;

			for (String team : ConfigManager.getTeams())
			{
				if (ConfigManager.getMembers(team).contains(player.getUniqueId().toString()))
				{
					playerIsAMember = true;
					break;
				}
				else if (ConfigManager.getLeader(team).equals(player.getUniqueId().toString()))
				{
					playerTeamName = team;
					break;
				}
				else if (ConfigManager.getExecutives(team).contains(player.getUniqueId().toString()))
				{
					playerTeamName = team;
					break;
				}
			}
			
			if (playerTeamName != null && !playerIsAMember)
			{
				if(Polis.autoClaim.contains(player.getUniqueId()))
				{
					player.sendMessage(Texts.of(TextColors.GREEN, "[Polis]: ", TextColors.GOLD, "Auto-Claim: ", TextColors.GRAY, "disabled."));
					Polis.autoClaim.remove(player.getUniqueId());
				}
				else
				{
					player.sendMessage(Texts.of(TextColors.GREEN, "[Polis]: ", TextColors.GOLD, "Auto-Claim: ", TextColors.GRAY, "enabled."));
					Polis.autoClaim.add(player.getUniqueId());
				}
			}
			else if(playerIsAMember)
			{
				player.sendMessage(Texts.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "Ask your leader to claim!"));
			}
			else
			{
				player.sendMessage(Texts.of(TextColors.GREEN, "[Polis]: ", TextColors.DARK_RED, "Error! ", TextColors.RED, "You are not in a town!"));
			}
		}
		else if (src instanceof ConsoleSource)
		{
			src.sendMessage(Texts.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "Must be an in-game player to use /polis autoclaim!"));
		}
		else if (src instanceof CommandBlockSource)
		{
			src.sendMessage(Texts.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "Must be an in-game player to use /polis autoclaim!"));
		}

		return CommandResult.success();
	}
}
