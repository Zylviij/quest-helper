/*
 * Copyright (c) 2021, Obasill <https://github.com/Obasill>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.questhelper.helpers.achievementdiaries.wilderness;

import com.questhelper.bank.banktab.BankSlotIcons;
import com.questhelper.collections.ItemCollections;
import com.questhelper.panel.PanelDetails;
import com.questhelper.questhelpers.ComplexStateQuestHelper;
import com.questhelper.questinfo.QuestHelperQuest;
import com.questhelper.requirements.ComplexRequirement;
import com.questhelper.requirements.Requirement;
import com.questhelper.requirements.conditional.Conditions;
import com.questhelper.requirements.item.ItemRequirement;
import com.questhelper.requirements.item.ItemRequirements;
import com.questhelper.requirements.player.SkillRequirement;
import com.questhelper.requirements.quest.QuestRequirement;
import com.questhelper.requirements.util.LogicType;
import com.questhelper.requirements.var.VarplayerRequirement;
import com.questhelper.requirements.zone.Zone;
import com.questhelper.requirements.zone.ZoneRequirement;
import com.questhelper.rewards.ItemReward;
import com.questhelper.rewards.UnlockReward;
import com.questhelper.steps.ConditionalStep;
import com.questhelper.steps.NpcStep;
import com.questhelper.steps.ObjectStep;
import com.questhelper.steps.QuestStep;
import net.runelite.api.QuestState;
import net.runelite.api.Skill;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.gameval.ItemID;
import net.runelite.api.gameval.NpcID;
import net.runelite.api.gameval.ObjectID;
import net.runelite.api.gameval.VarPlayerID;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class WildernessMedium extends ComplexStateQuestHelper
{
	// Items required
	ItemRequirement combatGear, pickaxe, runeAxe, antiDragonShield, unpoweredOrb, cosmicRune, earthRune, coins,
		goldBar, goldOre, hammer, muddyKey, godEquip, knife, goldHelmet, barsOrPick;

	Requirement enterGodwars;

	// Items recommended
	ItemRequirement food, burningAmulet, gamesNeck;

	// Quests required
	Requirement betweenARock;

	Requirement notMineMith, notEntYew, notWildyGodWars, notWildyAgi, notKillGreenDrag, notKillAnkou,
		notWildyGWBloodveld, notEmblemTrader, notGoldHelm, notMuddyChest, notEarthOrb;

	QuestStep claimReward, mineMith, wildyAgi, killAnkou, wildyGWBloodveld, emblemTrader, goldHelm, muddyChest,
		earthOrb, moveToResource, moveToGodWars1, moveToGodWars2, mineGoldOre, smeltGoldOre, moveToEdge, moveToSlayer1,
		moveToSlayer2, wildyGodwars;

	NpcStep entYew, killGreenDrag;

	Zone resource, godWars1, godWars2, slayer, edge;

	ZoneRequirement inResource, inGodWars1, inGodWars2, inSlayer, inEdge;

	ConditionalStep mineMithTask, entYewTask, wildyGodWarsTask, wildyAgiTask, killGreenDragTask, killAnkouTask,
		wildyGWBloodveldTask, emblemTraderTask, goldHelmTask, muddyChestTask, earthOrbTask;

	@Override
	public QuestStep loadStep()
	{
		initializeRequirements();
		setupSteps();

		ConditionalStep doMedium = new ConditionalStep(this, claimReward);

		entYewTask = new ConditionalStep(this, entYew);
		doMedium.addStep(notEntYew, entYewTask);

		killAnkouTask = new ConditionalStep(this, moveToSlayer1);
		killAnkouTask.addStep(inSlayer, killAnkou);
		doMedium.addStep(notKillAnkou, killAnkouTask);

		killGreenDragTask = new ConditionalStep(this, moveToSlayer2);
		killGreenDragTask.addStep(inSlayer, killGreenDrag);
		doMedium.addStep(notKillGreenDrag, killGreenDragTask);

		wildyGodWarsTask = new ConditionalStep(this, moveToGodWars1);
		wildyGodWarsTask.addStep(inGodWars1, wildyGodwars);
		doMedium.addStep(notWildyGodWars, wildyGodWarsTask);

		wildyGWBloodveldTask = new ConditionalStep(this, moveToGodWars2);
		wildyGWBloodveldTask.addStep(inGodWars1, wildyGodwars);
		wildyGWBloodveldTask.addStep(inGodWars2, wildyGWBloodveld);
		doMedium.addStep(notWildyGWBloodveld, wildyGWBloodveldTask);

		emblemTraderTask = new ConditionalStep(this, emblemTrader);
		doMedium.addStep(notEmblemTrader, emblemTraderTask);

		earthOrbTask = new ConditionalStep(this, moveToEdge);
		earthOrbTask.addStep(inEdge, earthOrb);
		doMedium.addStep(notEarthOrb, earthOrbTask);

		mineMithTask = new ConditionalStep(this, mineMith);
		doMedium.addStep(notMineMith, mineMithTask);

		wildyAgiTask = new ConditionalStep(this, wildyAgi);
		doMedium.addStep(notWildyAgi, wildyAgiTask);

		// TODO: IF in bank, step to drop
		goldHelmTask = new ConditionalStep(this, moveToResource);
		goldHelmTask.addStep(new Conditions(inResource, goldBar.quantity(3).alsoCheckBank(questBank)), goldHelm);
		goldHelmTask.addStep(new Conditions(inResource, goldOre.quantity(3)), smeltGoldOre);
		goldHelmTask.addStep(inResource, mineGoldOre);
		doMedium.addStep(notGoldHelm, goldHelmTask);

		muddyChestTask = new ConditionalStep(this, muddyChest);
		doMedium.addStep(notMuddyChest, muddyChestTask);

		return doMedium;
	}

	@Override
	protected void setupRequirements()
	{
		notMineMith = new VarplayerRequirement(VarPlayerID.WILDERNESS_ACHIEVEMENT_DIARY, false, 13);
		notEntYew = new VarplayerRequirement(VarPlayerID.WILDERNESS_ACHIEVEMENT_DIARY, false, 14);
		notWildyGodWars = new VarplayerRequirement(VarPlayerID.WILDERNESS_ACHIEVEMENT_DIARY, false, 15);
		notWildyAgi = new VarplayerRequirement(VarPlayerID.WILDERNESS_ACHIEVEMENT_DIARY, false, 16);
		notKillGreenDrag = new VarplayerRequirement(VarPlayerID.WILDERNESS_ACHIEVEMENT_DIARY, false, 18);
		notKillAnkou = new VarplayerRequirement(VarPlayerID.WILDERNESS_ACHIEVEMENT_DIARY, false, 19);
		notEarthOrb = new VarplayerRequirement(VarPlayerID.WILDERNESS_ACHIEVEMENT_DIARY, false, 20);
		notWildyGWBloodveld = new VarplayerRequirement(VarPlayerID.WILDERNESS_ACHIEVEMENT_DIARY, false, 21);
		notEmblemTrader = new VarplayerRequirement(VarPlayerID.WILDERNESS_ACHIEVEMENT_DIARY, false, 22);
		notGoldHelm = new VarplayerRequirement(VarPlayerID.WILDERNESS_ACHIEVEMENT_DIARY, false, 23);
		notMuddyChest = new VarplayerRequirement(VarPlayerID.WILDERNESS_ACHIEVEMENT_DIARY, false, 24);

		combatGear = new ItemRequirement("Combat gear", -1, -1);
		combatGear.setDisplayItemId(BankSlotIcons.getCombatGear());

		runeAxe = new ItemRequirement("Rune axe or better", ItemCollections.RUNE_AXE_BETTER)
			.showConditioned(notEntYew).isNotConsumed();
		antiDragonShield = new ItemRequirement("Anti-dragon shield", ItemCollections.ANTIFIRE_SHIELDS)
			.showConditioned(notKillGreenDrag).isNotConsumed();
		godEquip = new ItemRequirement("Various god equipment (1 of each god suggested)", -1, -1)
			.showConditioned(notWildyGWBloodveld).isNotConsumed();
		pickaxe = new ItemRequirement("Any pickaxe", ItemCollections.PICKAXES)
			.showConditioned(notMineMith).isNotConsumed();
		unpoweredOrb = new ItemRequirement("Unpowered orb", ItemID.STAFFORB).showConditioned(notEarthOrb);
		cosmicRune = new ItemRequirement("Cosmic rune", ItemID.COSMICRUNE).showConditioned(notEarthOrb);
		earthRune = new ItemRequirement("Earth rune", ItemID.EARTHRUNE).showConditioned(notEarthOrb);
		knife = new ItemRequirement("Knife or slashing weapon", -1, -1).isNotConsumed();
		muddyKey = new ItemRequirement("Muddy key", ItemID.MUDDY_KEY).showConditioned(notMuddyChest);
		goldHelmet = new ItemRequirement("Golden helmet not in inventory or bank (make sure this is red)",
			ItemID.DWARF_GOLDROCK_HELMET).showConditioned(notGoldHelm).isNotConsumed();
		coins = new ItemRequirement("Coins", ItemCollections.COINS).showConditioned(notGoldHelm);
		goldBar = new ItemRequirement("Gold bar", ItemID.GOLD_BAR).showConditioned(notGoldHelm);
		goldOre = new ItemRequirement("Gold ore", ItemID.GOLD_ORE);
		hammer = new ItemRequirement("Hammer", ItemID.HAMMER).showConditioned(notGoldHelm).isNotConsumed();
		barsOrPick = new ItemRequirements(LogicType.OR, "3 gold bars or a pickaxe", goldBar.quantity(3), pickaxe);

		food = new ItemRequirement("Food", ItemCollections.GOOD_EATING_FOOD, -1);
		burningAmulet = new ItemRequirement("Burning amulet", ItemCollections.BURNING_AMULETS);
		gamesNeck = new ItemRequirement("Games necklace", ItemCollections.GAMES_NECKLACES);

		enterGodwars = new ComplexRequirement(LogicType.OR, "60 Agility or Strength",
			new SkillRequirement(Skill.AGILITY, 60), new SkillRequirement(Skill.STRENGTH, 60));

		inEdge = new ZoneRequirement(edge);
		inResource = new ZoneRequirement(resource);
		inGodWars1 = new ZoneRequirement(godWars1);
		inGodWars2 = new ZoneRequirement(godWars2);
		inSlayer = new ZoneRequirement(slayer);

		betweenARock = new QuestRequirement(QuestHelperQuest.BETWEEN_A_ROCK, QuestState.IN_PROGRESS,
			"Schematic to make gold helmet in Between a Rock");
	}

	@Override
	protected void setupZones()
	{
		resource = new Zone(new WorldPoint(3174, 3944, 0), new WorldPoint(3196, 3924, 0));
		godWars1 = new Zone(new WorldPoint(3046, 10177, 3), new WorldPoint(3076, 10138, 3));
		godWars2 = new Zone(new WorldPoint(3014, 10168, 0), new WorldPoint(3069, 10115, 0));
		slayer = new Zone(new WorldPoint(3327, 10165, 0), new WorldPoint(3456, 10043, 0));
		edge = new Zone(new WorldPoint(3067, 10000, 0), new WorldPoint(3288, 9821, 0));
	}

	public void setupSteps()
	{
		entYew = new NpcStep(this, NpcID.WILDERNESS_ENT, new WorldPoint(3227, 3666, 0),
			"Kill an Ent in the wilderness and cut yew logs from its trunk after killing it.", combatGear, runeAxe);
		entYew.addAlternateNpcs(NpcID.ENT_TRUNK);

		moveToSlayer1 = new ObjectStep(this, ObjectID.WILD_SLAYER_CAVE_SOUTH_ENTRANCE , new WorldPoint(3260, 3665, 0),
			"Enter the Wilderness Slayer Cave.", combatGear, food);
		killAnkou = new NpcStep(this, NpcID.WILD_CAVE_ANKOU, new WorldPoint(3373, 10073, 0),
			"Kill an Ankou in the Wilderness Slayer Cave.", true, combatGear, food);

		moveToSlayer2 = new ObjectStep(this, ObjectID.WILD_SLAYER_CAVE_SOUTH_ENTRANCE , new WorldPoint(3260, 3665, 0),
			"Enter the Wilderness Slayer Cave.", combatGear, food, antiDragonShield);
		killGreenDrag = new NpcStep(this, NpcID.WILD_CAVE_GREEN_DRAGON, new WorldPoint(3412, 10066, 0),
			"Kill a Green dragon in the Wilderness Slayer Cave.", true, combatGear, food, antiDragonShield);
		killGreenDrag.addAlternateNpcs(NpcID.WILD_CAVE_GREEN_DRAGON2, NpcID.WILD_CAVE_GREEN_DRAGON3);

		moveToGodWars1 = new ObjectStep(this, ObjectID.WILDERNESS_GWD_ENTRANCE, new WorldPoint(3017, 3738, 0),
			"Enter the Wilderness God Wars Dungeon.", combatGear, food, godEquip);
		moveToGodWars2 = new ObjectStep(this, ObjectID.WILDERNESS_GWD_ENTRANCE, new WorldPoint(3017, 3738, 0),
			"Enter the Wilderness God Wars Dungeon.", combatGear, food, godEquip);
		wildyGodwars = new ObjectStep(this, ObjectID.WILDERNESS_GWD_CREVICE, new WorldPoint(3066, 10142, 3),
			"Use the crevice to enter the Wilderness God Wars Dungeon. The Strength entrance is to the West.",
			combatGear, food, godEquip);
		wildyGWBloodveld = new NpcStep(this, NpcID.GODWARS_BLOODVELD, new WorldPoint(3050, 10131, 0),
			"Kill a Bloodveld in the Wilderness God Wars Dungeon.", combatGear, food, godEquip);

		mineMith = new ObjectStep(this, ObjectID.MITHRILROCK2, new WorldPoint(3057, 3944, 0),
			"Mine mithril in the Wilderness.", pickaxe);

		wildyAgi = new ObjectStep(this, ObjectID.BALANCEGATE52A, new WorldPoint(2998, 3917, 0),
			"Complete a lap of the Wilderness Agility Course.");

		moveToEdge = new ObjectStep(this, ObjectID.TRAPDOOR_OPEN, new WorldPoint(3097, 3468, 0),
			"Enter to the Edgeville Dungeon.");
		earthOrb = new ObjectStep(this, ObjectID.OBELISK_EARTH, new WorldPoint(3087, 9933, 0),
			"Cast charge earth orb on the Obelisk of Earth.", unpoweredOrb, earthRune.quantity(30),
			cosmicRune.quantity(3));

		emblemTrader = new NpcStep(this, NpcID.EMBLEM_TRADER, new WorldPoint(3097, 3504, 0),
			"Speak with the Emblem Trader.");

		goldHelm = new ObjectStep(this, ObjectID.ANVIL, new WorldPoint(3190, 3938, 0),
			"Smith the gold helmet in the Resource Area. ", hammer, goldBar.quantity(3));
		moveToResource = new ObjectStep(this, ObjectID.WILDERNESS_RESOURCE_GATE, new WorldPoint(3184, 3944, 0),
			"Enter the Wilderness Resource Area. Check your bank to make sure you DO NOT have a gold helmet.",
			coins.quantity(7500), hammer, barsOrPick, goldHelmet);
		smeltGoldOre = new ObjectStep(this, ObjectID.WILDERNESS_RESOURCE_FURNACE, new WorldPoint(3191, 3936, 0),
			"Smelt the gold ore into gold bars.", hammer, goldOre.quantity(3));
		mineGoldOre = new ObjectStep(this, ObjectID.GOLDROCK1, new WorldPoint(3184, 3941, 0),
			"Mine gold ore.", true, hammer, pickaxe);

		muddyChest = new ObjectStep(this, ObjectID.MUDDY_CHESTCLOSED, new WorldPoint(3089, 3859, 0),
			"Use a Muddy key on the chest in the Lava Maze.", muddyKey, knife);

		claimReward = new NpcStep(this, NpcID.LESSER_FANATIC_DIARY, new WorldPoint(3121, 3518, 0),
			"Talk to Lesser Fanatic in Edgeville to claim your reward!");
		claimReward.addDialogStep("I have a question about my Achievement Diary.");
	}

	@Override
	public List<ItemRequirement> getItemRequirements()
	{
		return Arrays.asList(unpoweredOrb, cosmicRune.quantity(3), earthRune.quantity(30), pickaxe, antiDragonShield,
			runeAxe, combatGear, barsOrPick, hammer, coins.quantity(7500), knife, muddyKey);
	}

	@Override
	public List<ItemRequirement> getItemRecommended()
	{
		return Arrays.asList(food, gamesNeck, burningAmulet);
	}


	@Override
	public List<Requirement> getGeneralRequirements()
	{
		List<Requirement> reqs = new ArrayList<>();
		reqs.add(enterGodwars);
		reqs.add(new SkillRequirement(Skill.AGILITY, 52, true));
		reqs.add(new SkillRequirement(Skill.MAGIC, 60, true));
		reqs.add(new SkillRequirement(Skill.MINING, 55, true));
		reqs.add(new SkillRequirement(Skill.SLAYER, 50, true));
		reqs.add(new SkillRequirement(Skill.SMITHING, 50, true));
		reqs.add(new SkillRequirement(Skill.WOODCUTTING, 61, true));

		reqs.add(betweenARock);

		return reqs;
	}

	@Override
	public List<String> getCombatRequirements()
	{
		return Arrays.asList("Bloodveld (lvl 81)", "Green dragon (lvl 88)", "Ankou (lvl 98)",
			"Ent (lvl 101)");
	}

	@Override
	public List<ItemReward> getItemRewards()
	{
		return Arrays.asList(
			new ItemReward("Wilderness Sword 2", ItemID.WILDERNESS_SWORD_MEDIUM),
			new ItemReward("7,500 Exp. Lamp (Any skill over 40)", ItemID.THOSF_REWARD_LAMP)
		);
	}

	@Override
	public List<UnlockReward> getUnlockRewards()
	{
		return Arrays.asList(
			new UnlockReward("Increases the chance of a successful yield from ents by 15%"),
			new UnlockReward("20% off entry to Resource Area (6000gp)"),
			new UnlockReward("Can have 4 ecumenical keys at a time"),
			new UnlockReward("80 random free runes from Lundail once per day"),
			new UnlockReward("Access to shortcut in Deep Wilderness Dungeon"),
			new UnlockReward("Access to Spindel, Artio and Calvar'ion"),
			new UnlockReward("Players will receive standard quantity of loot from rogues' chests (rather than -25%)"),
			new UnlockReward("Players will roll on Zombie pirate drop table 50% of the time (rather than 15%)")
		);
	}

	@Override
	public List<PanelDetails> getPanels()
	{
		List<PanelDetails> allSteps = new ArrayList<>();

		PanelDetails entSteps = new PanelDetails("Ent Yew", Collections.singletonList(entYew),
			new SkillRequirement(Skill.WOODCUTTING, 61, true), combatGear, food, runeAxe);
		entSteps.setDisplayCondition(notEntYew);
		entSteps.setLockingStep(entYewTask);
		allSteps.add(entSteps);

		PanelDetails ankouSteps = new PanelDetails("Kill Ankou", Arrays.asList(moveToSlayer1, killAnkou), combatGear,
			food);
		ankouSteps.setDisplayCondition(notKillAnkou);
		ankouSteps.setLockingStep(killAnkouTask);
		allSteps.add(ankouSteps);

		PanelDetails greenDragSteps = new PanelDetails("Kill Green Dragon", Arrays.asList(moveToSlayer2,
			killGreenDrag), combatGear, food, antiDragonShield);
		greenDragSteps.setDisplayCondition(notKillGreenDrag);
		greenDragSteps.setLockingStep(killGreenDragTask);
		allSteps.add(greenDragSteps);

		PanelDetails godWarsSteps = new PanelDetails("Enter Wilderness God Wars", Arrays.asList(moveToGodWars1,
			wildyGodwars));
		godWarsSteps.setDisplayCondition(notWildyGodWars);
		godWarsSteps.setLockingStep(wildyGodWarsTask);
		allSteps.add(godWarsSteps);

		PanelDetails bloodveldSteps = new PanelDetails("Kill Bloodveld in God Wars Dungeon",
			Arrays.asList(moveToGodWars2, wildyGodwars, wildyGWBloodveld), new SkillRequirement(Skill.SLAYER, 50, true),
			enterGodwars, combatGear, food, godEquip);
		bloodveldSteps.setDisplayCondition(notWildyGWBloodveld);
		bloodveldSteps.setLockingStep(wildyGWBloodveldTask);
		allSteps.add(bloodveldSteps);

		PanelDetails emblemSteps = new PanelDetails("Emblem Trader", Collections.singletonList(emblemTrader));
		emblemSteps.setDisplayCondition(notEmblemTrader);
		emblemSteps.setLockingStep(emblemTraderTask);
		allSteps.add(emblemSteps);

		PanelDetails earthOrbSteps = new PanelDetails("Earth Orb", Arrays.asList(moveToEdge, earthOrb),
			new SkillRequirement(Skill.MAGIC, 60, true), unpoweredOrb, earthRune.quantity(30), cosmicRune.quantity(3));
		earthOrbSteps.setDisplayCondition(notEarthOrb);
		earthOrbSteps.setLockingStep(earthOrbTask);
		allSteps.add(earthOrbSteps);

		PanelDetails mithSteps = new PanelDetails("Mine Mithril", Collections.singletonList(mineMith),
			new SkillRequirement(Skill.MINING, 55, true), pickaxe, knife);
		mithSteps.setDisplayCondition(notMineMith);
		mithSteps.setLockingStep(mineMithTask);
		allSteps.add(mithSteps);

		PanelDetails wildyAgiSteps = new PanelDetails("Wilderness Agility Course",
			Collections.singletonList(wildyAgi), new SkillRequirement(Skill.AGILITY, 52, true), knife);
		wildyAgiSteps.setDisplayCondition(notWildyAgi);
		wildyAgiSteps.setLockingStep(wildyAgiTask);
		allSteps.add(wildyAgiSteps);

		PanelDetails goldHelmSteps = new PanelDetails("Gold Helmet in Resource Area", Arrays.asList(moveToResource,
			mineGoldOre, smeltGoldOre, goldHelm), new SkillRequirement(Skill.SMITHING, 50, true), betweenARock,
			coins.quantity(7500), barsOrPick, hammer, knife);
		goldHelmSteps.setDisplayCondition(notGoldHelm);
		goldHelmSteps.setLockingStep(goldHelmTask);
		allSteps.add(goldHelmSteps);

		PanelDetails chestSteps = new PanelDetails("Muddy Chest", Collections.singletonList(muddyChest), muddyKey,
			knife);
		chestSteps.setDisplayCondition(notMuddyChest);
		chestSteps.setLockingStep(muddyChestTask);
		allSteps.add(chestSteps);

		allSteps.add(new PanelDetails("Finishing off", Collections.singletonList(claimReward)));

		return allSteps;
	}
}
