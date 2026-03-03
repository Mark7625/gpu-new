package com.gpu.overlays;

import javax.annotation.Nonnull;
import lombok.RequiredArgsConstructor;

import static com.gpu.overlays.FrameTimer.*;

@RequiredArgsConstructor
public enum Timer {
	// CPU timers

	// Draw callbacks
	DRAW_FRAME,
	DRAW_PRESCENE,
	DRAW_SCENE,
	DRAW_ZONE_OPAQUE,
	DRAW_ZONE_ALPHA,
	DRAW_PASS,
	DRAW_POSTSCENE,
	DRAW_TILED_LIGHTING,
	GARBAGE_COLLECTION,
	// Miscellaneous
	SWAP_BUFFERS,
	EXECUTE_COMMAND_BUFFER,
	MAP_UI_BUFFER("Map UI Buffer"),
	COPY_UI("Copy UI"),

	// GPU timers
	RENDER_FRAME(GPU_TIMER),
	UPLOAD_UI(GPU_TIMER, "Upload UI"),
	RENDER_SCENE(GPU_TIMER),
	RENDER_UI(GPU_TIMER, "Render UI"),
	;

	public static final Timer[] TIMERS = values();
	public final String name;
	public final int type;

	Timer() {
		name = enumToName(name());
		type = CPU_TIMER;
	}

	Timer(int type) {
		name = enumToName(name());
		this.type = type;
	}

	Timer(@Nonnull String name) {
		this.name = name;
		type = CPU_TIMER;
	}

	Timer(int type, @Nonnull String name) {
		this.name = name;
		this.type = type;
	}

	private static String enumToName(String name) {
		name = name.replace('_', ' ');
		return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
	}

	public boolean isCpuTimer() {
		return type == CPU_TIMER;
	}

	public boolean isAsyncCpuTimer() {
		return type == ASYNC_CPU_TIMER;
	}

	public boolean isGpuTimer() {
		return type == GPU_TIMER || type == ASYNC_GPU_TIMER;
	}

	public boolean hasGpuDebugGroup() {
		return type == GPU_TIMER;
	}

	@Override
	public String toString() {
		return name;
	}
}