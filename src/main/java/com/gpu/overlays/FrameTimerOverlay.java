/*
 * Copyright (c) 2018, Adam <Adam@sigterm.info>
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
package com.gpu.overlays;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;
import com.gpu.GpuPluginGles;

@Singleton
public class FrameTimerOverlay extends OverlayPanel implements FrameTimer.Listener {
	@Inject
	private OverlayManager overlayManager;

	@Inject
	private GpuPluginGles plugin;

	@Inject
	private FrameTimer frameTimer;

	private final ArrayDeque<FrameTimings> frames = new ArrayDeque<>();
	private final long[] timings = new long[Timer.TIMERS.length];
	private float cpuLoad;
	private final Map<String, LineComponent> componentMap = new HashMap<>();
	private final StringBuilder sb = new StringBuilder();
	private final Formatter formatter = new Formatter(sb);

	@Inject
	public FrameTimerOverlay(GpuPluginGles plugin) {
		super(plugin);
		setLayer(OverlayLayer.ABOVE_SCENE);
		setPosition(OverlayPosition.TOP_RIGHT);
		panelComponent.setPreferredSize(new Dimension(240, 260));
	}

	public void setActive(boolean activate) {
		if (activate) {
			frameTimer.addTimingsListener(this);
			overlayManager.add(this);
		} else {
			frameTimer.removeTimingsListener(this);
			overlayManager.remove(this);
			frames.clear();
		}
	}

	private String format(String format, Object... args) {
		sb.setLength(0);
		formatter.format(format, args);
		return sb.toString();
	}

	private static String truncate(String s, int maxLen) {
		if (s == null || s.length() <= maxLen)
			return s;
		return s.substring(0, maxLen - 3) + "...";
	}

	@Override
	public void onFrameCompletion(FrameTimings timings) {
		long now = System.currentTimeMillis();
		while (!frames.isEmpty()) {
			if (now - frames.peekFirst().frameTimestamp < 10_000)
				break;
			frames.removeFirst();
		}
		frames.addLast(timings);
	}

	@Override
	public Dimension render(Graphics2D g) {
		long time = System.nanoTime();
		var boldFont = FontManager.getRunescapeBoldFont();

		var children = panelComponent.getChildren();
		if (!getAverageTimings()) {
			children.add(TitleComponent.builder()
				.text("Waiting for data...")
				.build());
		} else {
			long cpuTime = timings[Timer.DRAW_FRAME.ordinal()];
			long asyncCpuTime = 0;
			addTiming("CPU", cpuTime, true);
			for (var t : Timer.TIMERS) {
				if (t.isCpuTimer() && t != Timer.DRAW_FRAME)
					addTiming(t, timings);
				if (t.isAsyncCpuTimer())
					asyncCpuTime += timings[t.ordinal()];
			}

			addTiming("Async", asyncCpuTime, true);
			for (var t : Timer.TIMERS)
				if (t.isAsyncCpuTimer())
					addTiming(t, timings);

			if (cpuLoad > 0) {
				children.add(LineComponent.builder()
					.left("CPU Load:")
					.right((int) (cpuLoad * 100) + "%")
					.build());
			}

			children.add(TitleComponent.builder()
				.text("GPU")
				.build());

			String device = plugin.getRenderDevice();
			if (device != null && !device.isEmpty()) {
				children.add(LineComponent.builder()
					.left("Device:")
					.right(truncate(device, 28))
					.build());
			}
			String version = plugin.getRenderVersion();
			if (version != null && !version.isEmpty()) {
				children.add(LineComponent.builder()
					.left("Driver:")
					.right(truncate(version, 28))
					.build());
			}
			if (plugin.getAngleBackend() != null) {
				children.add(LineComponent.builder()
					.left("Backend:")
					.right(plugin.getAngleBackend().getName())
					.build());
			}

			long gpuTime = timings[Timer.RENDER_FRAME.ordinal()];
			if (gpuTime == 0) {
				for (var t : Timer.TIMERS)
					if (t.isGpuTimer())
						gpuTime += timings[t.ordinal()];
			}
			addTiming("GPU", gpuTime, true);

			long renderTime = timings[Timer.RENDER_SCENE.ordinal()] + timings[Timer.RENDER_UI.ordinal()];
			addTiming("Render", renderTime, true);

			addTiming(Timer.RENDER_SCENE, timings, "game world");
			addTiming(Timer.RENDER_UI, timings, "interface");
			for (var t : Timer.TIMERS)
				if (t.isGpuTimer() && t != Timer.RENDER_FRAME && t != Timer.RENDER_SCENE && t != Timer.RENDER_UI)
					addTiming(t, timings);

			children.add(LineComponent.builder()
				.leftFont(boldFont)
				.left("Estimated bottleneck:")
				.rightFont(boldFont)
				.right(cpuTime > gpuTime ? "CPU" : "GPU")
				.build());

			double fps = getActualFps();
			if (fps > 0) {
				children.add(LineComponent.builder()
					.leftFont(boldFont)
					.left("Actual FPS:")
					.rightFont(boldFont)
					.right(format("%.1f FPS", fps))
					.build());
			}
			long bottleneckNs = Math.max(cpuTime, gpuTime);
			if (bottleneckNs > 0) {
				children.add(LineComponent.builder()
					.left("Theoretical max FPS:")
					.right(format("%.1f FPS", 1e9 / bottleneckNs))
					.build());
			}

			children.add(LineComponent.builder()
				.left("Error compensation:")
				.right(format("%d ns", frameTimer.errorCompensation))
				.build());

			children.add(LineComponent.builder()
				.left("Garbage collection count:")
				.right(String.valueOf(plugin.getGarbageCollectionCount()))
				.build());
		}

		var result = super.render(g);
		frameTimer.cumulativeError += System.nanoTime() - time;
		return result;
	}

	private boolean getAverageTimings() {
		if (frames.isEmpty())
			return false;

		Arrays.fill(timings, 0);
		cpuLoad = 0;
		for (var frame : frames) {
			for (int i = 0; i < frame.timers.length; i++)
				timings[i] += frame.timers[i];
			cpuLoad += frame.cpuLoad;
		}

		for (int i = 0; i < timings.length; i++)
			timings[i] = Math.max(0, timings[i] / frames.size());
		cpuLoad /= frames.size();

		return true;
	}

	private double getActualFps() {
		if (frames.size() < 2)
			return 0;
		// Use the two most recent frames to compute actual frame interval
		var it = frames.descendingIterator();
		long latest = it.next().frameTimestamp;
		long previous = it.next().frameTimestamp;
		long intervalMs = latest - previous;
		if (intervalMs <= 0)
			return 0;
		return 1000.0 / intervalMs;
	}

	private void addTiming(Timer timer, long[] timings) {
		addTiming(timer.name, timings[timer.ordinal()], false);
	}

	private void addTiming(Timer timer, long[] timings, String desc) {
		long nanos = timings[timer.ordinal()];
		if (nanos == 0)
			return;
		String label = timer.name + " (" + desc + ")";
		addTiming(label, nanos, false);
	}

	private void addTiming(String name, long nanos, boolean bold) {
		if (nanos == 0)
			return;

		String result = "~0 ms";
		if (Math.abs(nanos) > 1e3) {
			sb.setLength(0);
			result = sb.append(Math.round(nanos / 1e3) / 1e3).append(" ms").toString();
		}

		LineComponent component = componentMap.get(name);
		if (component == null) {
			var font = bold ? FontManager.getRunescapeBoldFont() : FontManager.getRunescapeFont();
			component = LineComponent.builder()
				.left(name + ":")
				.leftFont(font)
				.right(result)
				.rightFont(font)
				.build();
			componentMap.put(name, component);
		} else {
			component.setRight(result);
		}

		panelComponent.getChildren().add(component);
	}
}
