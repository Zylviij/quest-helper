/*
 * Copyright (c) 2021, Zoinkwiz <https://github.com/Zoinkwiz>
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
package com.questhelper.questhelpers;

import com.questhelper.QuestHelperConfig;
import com.questhelper.panel.PanelDetails;
import com.questhelper.steps.QuestStep;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class ComplexStateQuestHelper extends QuestHelper
{
	protected QuestStep step;
	protected int var;

	@Override
	public void init()
	{
		if (step == null)
		{
			step = loadStep();
		}
	}

	@Override
	public void startUp(QuestHelperConfig config)
	{
		step = loadStep();
		this.config = config;
		instantiateSteps(Collections.singletonList(step));
		var = getVar();
		sidebarOrder = questHelperPlugin.loadSidebarOrder(this);
		startUpStep(step);
	}

	@Override
	public void shutDown()
	{
		super.shutDown();
		shutDownStep();
	}

	@Override
	public boolean updateQuest()
	{
		return true;
	}

	public List<PanelDetails> getPanels()
	{
		return new ArrayList<>();
	}

	public abstract QuestStep loadStep();
}
