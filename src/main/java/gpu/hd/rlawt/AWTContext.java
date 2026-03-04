/*
 * Copyright (c) 2022 Abex
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
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
 *
 * Matches native rlawt exports (Java_gpu_hd_rlawt_AWTContext_*). Uses JAWT for
 * native window handle - no JNA required.
 */
package gpu.hd.rlawt;

import java.awt.Component;
import java.awt.Insets;
import java.awt.Window;
import java.io.InputStream;
import java.lang.annotation.Native;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public final class AWTContext
{
	private static boolean nativesLoaded = false;

	@Native
	private long instance;

	public static synchronized void loadNatives()
	{
		if (nativesLoaded)
		{
			return;
		}

		System.loadLibrary("jawt");

		String os = System.getProperty("os.name", "").toLowerCase();
		if (os.contains("win"))
		{
			loadAngleFromResources();
		}

		String overridePath = System.getProperty("runelite.rlawtpath");
		if (overridePath == null)
		{
			overridePath = System.getProperty("gpu.hd.rlawt.path");
		}
		if (overridePath != null)
		{
			if (os.contains("win"))
			{
				Path rlawtDir = Path.of(overridePath).getParent();
				if (rlawtDir != null)
				{
					for (String dll : new String[]{"d3dcompiler_47.dll", "libEGL.dll", "libGLESv2.dll"})
					{
						Path p = rlawtDir.resolve(dll);
						if (Files.exists(p))
							System.load(p.toAbsolutePath().toString());
					}
				}
			}
			System.load(overridePath);
			nativesLoaded = true;
			return;
		}
	}

	private static native long create0(Component component);

	public AWTContext(Component component)
	{
		this.instance = create0(component);
		if (instance == 0)
		{
			throw new NullPointerException();
		}

		int x = 0;
		int y = 0;
		for (Component c = component.getParent(); c != null; c = c.getParent())
		{
			if (c instanceof Window)
			{
				Insets insets = ((Window) c).getInsets();
				x -= insets.left;
				y -= insets.top;
				break;
			}

			x += c.getX();
			y += c.getY();
		}
		configureInsets(x, y);
	}

	public native void destroy();

	public native void configureInsets(int x, int y);

	public native void configurePixelFormat(int alpha, int depth, int stencil);

	public native void configureMultisamples(int samples);

	/** Sets if EGL should be used (required for GLES). Native signature: (ZJ)V */
	public native void useEGL(boolean egl, long eglGetProcAddress);

	/** Sets if OpenGL ES should be used. GLES is only available if EGL is used. */
	public native void useGLES(boolean es);

	/** Set ANGLE backend: 0=default, 1=D3D11, 2=D3D9, 3=Vulkan, 4=OpenGL. Windows only. */
	public native void setAngleBackend(int backend);

	/** Returns actual backend used (0-4). Call after createGLContext. */
	public native int getAngleBackend();

	public native int getFramebuffer(boolean front);

	public int getBufferMode()
	{
		final int GL_FRONT = 0x404;
		final int GL_COLOR_ATTACHMENT0 = 0x8CE0;

		return getFramebuffer(true) == 0 ? GL_FRONT : GL_COLOR_ATTACHMENT0;
	}

	public native void createGLContext();

	public native int setSwapInterval(int interval);

	public native void makeCurrent();

	public native void detachCurrent();

	public native void swapBuffers();

	public native long getGLContext();

	public native long getCGLShareGroup();

	public native long getGLXDisplay();

	public native long getWGLHDC();

	private static void loadAngleFromResources()
	{
		try
		{
			String[] dlls = {"d3dcompiler_47.dll", "libEGL.dll", "libGLESv2.dll"};
			for (String dll : dlls)
			{
				try (InputStream in = AWTContext.class.getResourceAsStream("/native/angle/" + dll))
				{
					if (in == null)
					{
						return;
					}
				}
			}
			Path angleDir = Files.createTempDirectory("angle-");
			angleDir.toFile().deleteOnExit();
			for (String dll : dlls)
			{
				try (InputStream in = AWTContext.class.getResourceAsStream("/native/angle/" + dll))
				{
					if (in != null)
					{
						Path out = angleDir.resolve(dll);
						Files.copy(in, out, StandardCopyOption.REPLACE_EXISTING);
						out.toFile().deleteOnExit();
					}
				}
			}
			if (System.getProperty("os.name", "").toLowerCase().contains("win"))
			{
				String vulkanSdk = System.getenv("VULKAN_SDK");
				if (vulkanSdk != null)
				{
					for (String sub : new String[]{"Bin", "Bin/x64", "Bin/Win32"})
					{
						Path sdkLoader = Path.of(vulkanSdk, sub, "vulkan-1.dll");
						if (sdkLoader.toFile().exists())
						{
							System.load(sdkLoader.toAbsolutePath().toString());
							break;
						}
					}
				}
			}
			System.load(angleDir.resolve("libEGL.dll").toAbsolutePath().toString());
			System.load(angleDir.resolve("libGLESv2.dll").toAbsolutePath().toString());
		}
		catch (Throwable ignored)
		{
		}
	}
}
