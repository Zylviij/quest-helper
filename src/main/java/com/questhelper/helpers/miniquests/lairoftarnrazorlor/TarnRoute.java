/*
 * Copyright (c) 2020, Zoinkwiz
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
package com.questhelper.helpers.miniquests.lairoftarnrazorlor;

import com.questhelper.bank.banktab.BankSlotIcons;
import com.questhelper.questhelpers.QuestHelper;
import com.questhelper.questhelpers.QuestUtil;
import com.questhelper.requirements.Requirement;
import com.questhelper.requirements.conditional.Conditions;
import com.questhelper.requirements.conditional.NpcCondition;
import com.questhelper.requirements.conditional.ObjectCondition;
import com.questhelper.requirements.item.ItemRequirement;
import com.questhelper.requirements.player.PrayerRequirement;
import com.questhelper.requirements.util.Operation;
import com.questhelper.requirements.var.VarbitRequirement;
import com.questhelper.requirements.zone.Zone;
import com.questhelper.requirements.zone.ZoneRequirement;
import com.questhelper.steps.ConditionalStep;
import com.questhelper.steps.DetailedQuestStep;
import com.questhelper.steps.ObjectStep;
import com.questhelper.steps.QuestStep;
import net.runelite.api.Prayer;
import net.runelite.api.SpriteID;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.gameval.ItemID;
import net.runelite.api.gameval.NpcID;
import net.runelite.api.gameval.ObjectID;
import net.runelite.api.gameval.VarbitID;

import java.util.Arrays;
import java.util.List;

public class TarnRoute extends ConditionalStep
{
	//Requirements
	ItemRequirement combatGear, diary;

	PrayerRequirement protectFromMagic;

	Requirement inHauntedMine, inRoom1, inRoom1PastTrap1, inRoom1PastTrap2, inRoom2, inRoom3, inRoom4, inRoom5, inRoom6P1, inRoom6P2,
		onPillar1, onPillar2, onPillar3, onPillar4, onPillar5, onPillar6, atSwitch1, switchPressed, inRoom6PastTrap1, inRoom6PastTrap2,
		inExtraRoom1, inExtraRoom2, inRoom7, inRoom8, inBossRoom, tarnInSecondForm, killedTarn, inFinalRoom;

	QuestStep enterHauntedMine;

	DetailedQuestStep enterLair, searchWallRoom1, searchWall2Room1, goThroughRoom1, goThroughRoom2, goThroughRoom3,
		goThroughRoom4, goThroughRoom5, jumpToPillar1, jumpToPillar2, jumpToPillar3, jumpToPillar4, jumpToPillar5, jumpToPillar6,
		jumpToSwitch, pressSwitch, jumpBackToPillar4, jumpBackToPillar3, jumpToNorthLedge, searchWallRoom6, searchWall2Room6, goThroughRoom6,
		leaveExtraRoom1, leaveExtraRoom2, goThroughRoom7, enterBossRoom;

	//Zones
	Zone hauntedMine, room1, room1PastTrap1, room1PastTrap2, room2, room3, room4, room5P1, room5P2, room6P1, room6P2, room6P3, pillar1, pillar2, pillar3,
		pillar4, switch1, pillar5, pillar6, room6PastTrap1, room6PastTrap2P1, room6PastTrap2P2, extraRoom1, extraRoom2, room7, room8, bossRoom, finalRoom;


	public TarnRoute(QuestHelper questHelper)
	{
		super(questHelper, new ObjectStep(questHelper, ObjectID.HAUNTEDMINE_SECONDARY_ENTRANCE, new WorldPoint(3440, 3232, 0), "Enter the Haunted Mine. " +
			"If you have a Slayer Ring, instead teleport to Tarn's Lair to skip most of this."));
		setupRequirements();
		setupZones();
		setupConditions();
		setupSteps();

		addStep(inRoom8, enterBossRoom);
		addStep(inRoom7, goThroughRoom7);
		addStep(inRoom6PastTrap2, goThroughRoom6);
		addStep(inRoom6PastTrap1, searchWall2Room6);
		addStep(inRoom6P2, searchWallRoom6);
		addStep(onPillar6, jumpToNorthLedge);
		addStep(onPillar5, jumpToPillar6);
		addStep(new Conditions(onPillar3, switchPressed), jumpToPillar5);
		addStep(new Conditions(onPillar4, switchPressed), jumpBackToPillar3);
		addStep(new Conditions(atSwitch1, switchPressed), jumpBackToPillar4);
		addStep(atSwitch1, pressSwitch);
		addStep(onPillar4, jumpToSwitch);
		addStep(onPillar3, jumpToPillar4);
		addStep(onPillar2, jumpToPillar3);
		addStep(onPillar1, jumpToPillar2);
		addStep(inRoom6P1, jumpToPillar1);
		addStep(inRoom5, goThroughRoom5);
		addStep(inRoom4, goThroughRoom4);
		addStep(inRoom3, goThroughRoom3);
		addStep(inRoom2, goThroughRoom2);
		addStep(inRoom1PastTrap2, goThroughRoom1);
		addStep(inRoom1PastTrap1, searchWall2Room1);
		addStep(inRoom1, searchWallRoom1);
		addStep(inHauntedMine, enterLair);
	}

	public void setupRequirements()
	{
		combatGear = new ItemRequirement("Combat gear", -1, -1);
		combatGear.setDisplayItemId(BankSlotIcons.getCombatGear());
		diary = new ItemRequirement("Tarn's diary", ItemID.LOTR_DIARY);
		diary.setHighlightInInventory(true);
		protectFromMagic = new PrayerRequirement("Activate Protect from Magic", Prayer.PROTECT_FROM_MAGIC);
	}

	public void setupZones()
	{
		hauntedMine = new Zone(new WorldPoint(3390, 9600, 0), new WorldPoint(3452, 9668, 0));
		room1 = new Zone(new WorldPoint(3136, 4544, 0), new WorldPoint(3199, 4570, 0));
		room1PastTrap1 = new Zone(new WorldPoint(3196, 4558, 0), new WorldPoint(3196, 4562, 0));
		room1PastTrap2 = new Zone(new WorldPoint(3193, 4563, 0), new WorldPoint(3196, 4570, 0));

		room2 = new Zone(new WorldPoint(3172, 4574, 1), new WorldPoint(3197, 4589, 1));
		room3 = new Zone(new WorldPoint(3166, 4575, 0), new WorldPoint(3170, 4579, 0));
		room4 = new Zone(new WorldPoint(3166, 4586, 0), new WorldPoint(3170, 4592, 0));
		room5P1 = new Zone(new WorldPoint(3143, 4589, 1), new WorldPoint(3162, 4590, 1));
		room5P2 = new Zone(new WorldPoint(3154, 4590, 1), new WorldPoint(3157, 4598, 1));

		room6P1 = new Zone(new WorldPoint(3136, 4592, 1), new WorldPoint(3151, 4600, 1));

		room6P2 = new Zone(new WorldPoint(3141, 4601, 1), new WorldPoint(3163, 4607, 1));
		room6P3 = new Zone(new WorldPoint(3159, 4596, 1), new WorldPoint(3175, 4602, 1));

		pillar1 = new Zone(new WorldPoint(3148, 4595, 1), new WorldPoint(3148, 4595, 1));
		pillar2 = new Zone(new WorldPoint(3146, 4595, 1), new WorldPoint(3146, 4595, 1));
		pillar3 = new Zone(new WorldPoint(3144, 4595, 1), new WorldPoint(3144, 4595, 1));
		pillar4 = new Zone(new WorldPoint(3142, 4595, 1), new WorldPoint(3142, 4595, 1));

		pillar5 = new Zone(new WorldPoint(3144, 4597, 1), new WorldPoint(3144, 4597, 1));
		pillar6 = new Zone(new WorldPoint(3144, 4599, 1), new WorldPoint(3144, 4599, 1));

		switch1 = new Zone(new WorldPoint(3137, 4593, 1), new WorldPoint(3140, 4601, 1));

		room6PastTrap1 = new Zone(new WorldPoint(3150, 4604, 1), new WorldPoint(3153, 4604, 1));
		room6PastTrap2P1 = new Zone(new WorldPoint(3154, 4604, 1), new WorldPoint(3160, 4604, 1));
		room6PastTrap2P2 = new Zone(new WorldPoint(3159, 4596, 1), new WorldPoint(3175, 4602, 1));

		extraRoom1 = new Zone(new WorldPoint(3160, 4597, 0), new WorldPoint(3173, 4599, 0));
		extraRoom2 = new Zone(new WorldPoint(3140, 4594, 0), new WorldPoint(3150, 4601, 0));

		room7 = new Zone(new WorldPoint(3179, 4593, 1), new WorldPoint(3195, 4602, 1));

		room8 = new Zone(new WorldPoint(3181, 4595, 0), new WorldPoint(3189, 4601, 0));
		bossRoom = new Zone(new WorldPoint(3176, 4611, 0), new WorldPoint(3196, 4626, 0));
		finalRoom = new Zone(new WorldPoint(3181, 4632, 0), new WorldPoint(3191, 4637, 0));
	}

	public void setupConditions()
	{
		inHauntedMine = new ZoneRequirement(hauntedMine);
		inRoom1 = new ZoneRequirement(room1);
		inRoom1PastTrap1 = new ZoneRequirement(room1PastTrap1);
		inRoom1PastTrap2 = new ZoneRequirement(room1PastTrap2);
		inRoom2 = new ZoneRequirement(room2);
		inRoom3 = new ZoneRequirement(room3);
		inRoom4 = new ZoneRequirement(room4);
		inRoom5 = new ZoneRequirement(room5P1, room5P2);
		inRoom6P1 = new ZoneRequirement(room6P1);
		inRoom6P2 = new ZoneRequirement(room6P2, room6P3);
		inRoom7 = new ZoneRequirement(room7);
		onPillar1 = new ZoneRequirement(pillar1);
		onPillar2 = new ZoneRequirement(pillar2);
		onPillar3 = new ZoneRequirement(pillar3);
		onPillar4 = new ZoneRequirement(pillar4);
		onPillar5 = new ZoneRequirement(pillar5);
		onPillar6 = new ZoneRequirement(pillar6);
		atSwitch1 = new ZoneRequirement(switch1);
		switchPressed = new ObjectCondition(ObjectID.LOTR_TRAP_BUTTON_PRESSED_LVL1, new WorldPoint(3138, 4595, 1));
		inRoom6PastTrap1 = new ZoneRequirement(room6PastTrap1);
		inRoom6PastTrap2 = new ZoneRequirement(room6PastTrap2P1, room6PastTrap2P2);
		inExtraRoom1 = new ZoneRequirement(extraRoom1);
		inExtraRoom2 = new ZoneRequirement(extraRoom2);
		inRoom8 = new ZoneRequirement(room8);
		inBossRoom = new ZoneRequirement(bossRoom);
		inFinalRoom = new ZoneRequirement(finalRoom);

		tarnInSecondForm = new NpcCondition(NpcID.LOTR_TRAN_RAZORLOR);
		killedTarn = new VarbitRequirement(VarbitID.LOTR_INSTANCE_ENTERED, 2, Operation.GREATER_EQUAL);
	}

	public void setupSteps()
	{
		enterHauntedMine = steps.get(null);
		enterLair = new ObjectStep(questHelper, ObjectID.LOTR_ENTRANCE_DOOR_UNBLOCKED, new WorldPoint(3424, 9661, 0), "Enter the entrance to the north.");

		searchWallRoom1 = new ObjectStep(questHelper, ObjectID.LOTR_TRAP_SPEAR_WALL, new WorldPoint(3196, 4557, 0), "Follow the path east then north, and go through the door you reach.");
		searchWallRoom1.setLinePoints(Arrays.asList(
			new WorldPoint(3166, 4547, 0),
			new WorldPoint(3166, 4550, 0),
			new WorldPoint(3175, 4550, 0),
			new WorldPoint(3175, 4549, 0),
			new WorldPoint(3184, 4549, 0),
			new WorldPoint(3184, 4548, 0),
			new WorldPoint(3189, 4548, 0),
			new WorldPoint(3190, 4547, 0),
			new WorldPoint(3191, 4548, 0),
			new WorldPoint(3190, 4550, 0),
			new WorldPoint(3190, 4555, 0),
			new WorldPoint(3196, 4555, 0),
			new WorldPoint(3196, 4556, 0)
		));

		searchWall2Room1 = new ObjectStep(questHelper, ObjectID.LOTR_TRAP_SPEAR_WALL, new WorldPoint(3197, 4562, 0), "Follow the path west then north, and go through the door you reach.");

		goThroughRoom1 = new ObjectStep(questHelper, ObjectID.LOTR_RUINS_DOOR_19A, new WorldPoint(3195, 4571, 0), "Follow the path west then north, and go through the door you reach.");
		goThroughRoom1.addSubSteps(searchWallRoom1, searchWall2Room1);

		goThroughRoom2 = new ObjectStep(questHelper, ObjectID.LOTR_RUINS_DOOR_17A, new WorldPoint(3174, 4577, 1), "Continue to the west of the room.");
		goThroughRoom2.setLinePoints(Arrays.asList(
			new WorldPoint(3195, 4575, 1),
			new WorldPoint(3195, 4579, 1),
			new WorldPoint(3196, 4580, 1),
			new WorldPoint(3195, 4581, 1),
			new WorldPoint(3195, 4585, 1),
			new WorldPoint(3182, 4585, 1),
			new WorldPoint(3181, 4586, 1),
			new WorldPoint(3176, 4586, 1),
			new WorldPoint(3176, 4583, 1),
			new WorldPoint(3175, 4582, 1),
			new WorldPoint(3175, 4577, 1)
		));

		goThroughRoom3 = new ObjectStep(questHelper, ObjectID.LOTR_RUINS_DOOR_22A, new WorldPoint(3168, 4580, 0), "Go through the north door.");
		goThroughRoom4 = new ObjectStep(questHelper, ObjectID.LOTR_RUINS_DOOR_23A, new WorldPoint(3165, 4589, 0), "Go through the west door.");

		leaveExtraRoom1 = new ObjectStep(questHelper, ObjectID.LOTR_RUINS_DOOR_26A, new WorldPoint(3168, 4596, 0), "Go into the south door.");
		leaveExtraRoom2 = new ObjectStep(questHelper, ObjectID.LOTR_RUINS_DOOR_25A, new WorldPoint(3150, 4598, 0), "Go into the east passageway.");

		goThroughRoom5 = new ObjectStep(questHelper, ObjectID.LOTR_RUINS_DOOR_27A, new WorldPoint(3154, 4597, 1), "Go through the north door.");
		goThroughRoom5.addSubSteps(leaveExtraRoom1, leaveExtraRoom2);

		jumpToPillar1 = new ObjectStep(questHelper, ObjectID.LOTR_RUINS_PILLAR_JUMP_04, new WorldPoint(3148, 4595, 1), "Jump across the pillars to the west ledge.");
		jumpToPillar2 = new ObjectStep(questHelper, ObjectID.LOTR_RUINS_PILLAR_JUMP_05, new WorldPoint(3146, 4595, 1), "Jump across the pillars.");
		jumpToPillar3 = new ObjectStep(questHelper, ObjectID.LOTR_RUINS_PILLAR_JUMP_06, new WorldPoint(3144, 4595, 1), "Jump across the pillars.");
		jumpToPillar4 = new ObjectStep(questHelper, ObjectID.LOTR_RUINS_PILLAR_JUMP_07, new WorldPoint(3142, 4595, 1), "Jump across the pillars.");

		jumpToSwitch = new ObjectStep(questHelper, ObjectID.LOTR_RUINS_FLOOR_EDGE_JUMP_05, new WorldPoint(3140, 4595, 1), "Jump to the ledge.");
		jumpToPillar1.addSubSteps(jumpToPillar2, jumpToPillar3, jumpToPillar4, jumpToSwitch);

		pressSwitch = new ObjectStep(questHelper, ObjectID.LOTR_TRAP_BUTTON_LVL1, new WorldPoint(3138, 4595, 1), "Search the floor.");

		jumpBackToPillar4 = new ObjectStep(questHelper, ObjectID.LOTR_RUINS_PILLAR_JUMP_07, new WorldPoint(3142, 4595, 1), "Jump across the pillars to the north side.");
		jumpBackToPillar3 = new ObjectStep(questHelper, ObjectID.LOTR_RUINS_PILLAR_JUMP_06, new WorldPoint(3144, 4595, 1), "Jump across the pillars to the north side.");
		jumpToPillar5 = new ObjectStep(questHelper, ObjectID.LOTR_RUINS_PILLAR_JUMP_08, new WorldPoint(3144, 4597, 1), "Jump across the pillars to the north side.");
		jumpToPillar6 = new ObjectStep(questHelper, ObjectID.LOTR_RUINS_PILLAR_JUMP_09, new WorldPoint(3144, 4599, 1), "Jump across the pillars to the north side.");

		jumpToNorthLedge = new ObjectStep(questHelper, ObjectID.LOTR_RUINS_FLOOR_EDGE_JUMP_06, new WorldPoint(3144, 4601, 1), "Jump across the pillars to the north side.");
		jumpToNorthLedge.addSubSteps(jumpBackToPillar3, jumpBackToPillar4, jumpToPillar5, jumpToPillar6);

		searchWallRoom6 = new ObjectStep(questHelper, ObjectID.LOTR_TRAP_SPEAR_WALL_LVL1, new WorldPoint(3149, 4604, 1), "Follow the path to the east.");
		searchWall2Room6 = new ObjectStep(questHelper, ObjectID.LOTR_TRAP_SPEAR_WALL_LVL1, new WorldPoint(3154, 4605, 1), "Follow the path to the east.");

		goThroughRoom6 = new ObjectStep(questHelper, ObjectID.LOTR_RUINS_DOOR_28A, new WorldPoint(3176, 4598, 1), "Follow the path to the east. Avoid the walls which will occasionally stick out and knock you down, marked with a skull.");
		goThroughRoom6.addTileMarker(new WorldPoint(3162, 4600, 1), SpriteID.PLAYER_KILLER_SKULL);
		goThroughRoom6.addTileMarker(new WorldPoint(3164, 4600, 1), SpriteID.PLAYER_KILLER_SKULL);
		goThroughRoom6.addTileMarker(new WorldPoint(3171, 4600, 1), SpriteID.PLAYER_KILLER_SKULL);
		goThroughRoom6.addTileMarker(new WorldPoint(3173, 4600, 1), SpriteID.PLAYER_KILLER_SKULL);
		goThroughRoom6.addSubSteps(searchWallRoom6, searchWall2Room6);

		goThroughRoom7 = new ObjectStep(questHelper, ObjectID.LOTR_RUINS_STAIRS_LVL1, new WorldPoint(3193, 4598, 1), "Activate Protect from Magic and jump across the pillars. Go down the stairs.", protectFromMagic);

		enterBossRoom = new ObjectStep(questHelper, ObjectID.LOTR_RUINS_DOOR_30, new WorldPoint(3185, 4602, 0), "Enter the north passageway, and be prepared to fight.");
	}

	public List<QuestStep> getDisplaySteps()
	{
		return QuestUtil.toArrayList(enterHauntedMine, enterLair, goThroughRoom1, goThroughRoom2, goThroughRoom3,
			goThroughRoom4, goThroughRoom5, jumpToPillar1, pressSwitch, jumpToNorthLedge, goThroughRoom6, goThroughRoom7, enterBossRoom);
	}
}
