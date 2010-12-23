package com.jetbrains.python.debugger;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.intellij.xdebugger.XSourcePosition;
import com.intellij.xdebugger.breakpoints.XBreakpointHandler;
import com.intellij.xdebugger.breakpoints.XBreakpointProperties;
import com.intellij.xdebugger.breakpoints.XLineBreakpoint;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;


public class PyLineBreakpointHandler extends XBreakpointHandler<XLineBreakpoint<XBreakpointProperties>> {

  private final PyDebugProcess myDebugProcess;

  private final Map<XLineBreakpoint<XBreakpointProperties>, XSourcePosition> myBreakPointPositions = Maps.newHashMap();

  public PyLineBreakpointHandler(@NotNull final PyDebugProcess debugProcess) {
    super(PyLineBreakpointType.class);
    myDebugProcess = debugProcess;
  }

  public void reregisterBreakpoints() {
    List<XLineBreakpoint<XBreakpointProperties>> breakpoints = Lists.newArrayList(myBreakPointPositions.keySet());
    for (XLineBreakpoint<XBreakpointProperties> breakpoint : breakpoints) {
      unregisterBreakpoint(breakpoint, false);
      registerBreakpoint(breakpoint);
    }
  }

  public void registerBreakpoint(@NotNull final XLineBreakpoint<XBreakpointProperties> breakpoint) {
    final XSourcePosition position = breakpoint.getSourcePosition();
    if (position != null) {
      myDebugProcess.addBreakpoint(myDebugProcess.getPositionConverter().convert(position), breakpoint);
      myBreakPointPositions.put(breakpoint, position);
    }
  }

  public void unregisterBreakpoint(@NotNull final XLineBreakpoint<XBreakpointProperties> breakpoint, final boolean temporary) {
    final XSourcePosition position = myBreakPointPositions.get(breakpoint);
    if (position != null) {
      myDebugProcess.removeBreakpoint(myDebugProcess.getPositionConverter().convert(position));
      myBreakPointPositions.remove(breakpoint);
    }
  }
}
