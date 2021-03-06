// Copyright 2000-2018 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.intellij.testGuiFramework.framework.dtrace

import com.intellij.testGuiFramework.framework.GuiTestLocalRunner
import com.intellij.testGuiFramework.framework.GuiTestSuiteRunner
import com.intellij.testGuiFramework.launcher.GuiTestLocalLauncher
import com.intellij.testGuiFramework.launcher.ide.Ide
import com.intellij.testGuiFramework.remote.IdeProcessControlManager
import org.apache.log4j.Logger
import org.junit.runners.model.RunnerBuilder

class GuiDTTestSuiteRunner(suiteClass: Class<*>, builder: RunnerBuilder) : GuiTestSuiteRunner(suiteClass, builder) {
  private val LOG: Logger = org.apache.log4j.Logger.getLogger("#com.intellij.testGuiFramework.framework.dtrace.GuiDTTestSuiteRunner")!!

  override fun firstStart() {
    if (myFirstStartClassName == UNDEFINED_FIRST_CLASS) return
    LOG.info("IDE is configuring for the first time...")
    GuiTestLocalLauncher.firstStartIdeLocally(myIde, myFirstStartClassName)
    isFirstStart = false
  }

  override fun createGuiTestLocalRunner(testClass:Class<*>, suiteClass:Class<*>, myIde: Ide): GuiTestLocalRunner {
    IdeProcessControlManager.killIdeProcess()
    return GuiDTTestLocalRunner(testClass, suiteClass, myIde)
  }

}
