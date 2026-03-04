/*
 * Copyright (c) 2025
 * All rights reserved.
 */
package com.gpu.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/** ANGLE render backend (Windows). Requires local ANGLE build with all backends enabled. */
@Getter
@RequiredArgsConstructor
public enum AngleBackend
{
	DEFAULT("Default (D3D11)"),
	D3D11("Direct3D 11"),
	D3D9("Direct3D 9"),
	VULKAN("Vulkan"),
	OPENGL("Desktop OpenGL");

	private final String name;

	@Override
	public String toString()
	{
		return name;
	}

	/** Native backend id: 0=default, 1=D3D11, 2=D3D9, 3=Vulkan, 4=OpenGL */
	public int toNative()
	{
		return ordinal();
	}

	public static AngleBackend fromNative(int id)
	{
		AngleBackend[] v = values();
		return id >= 0 && id < v.length ? v[id] : DEFAULT;
	}
}
