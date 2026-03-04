/*
 * GL API - OpenGL ES 3.0 only (via ANGLE).
 */
package com.gpu;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.opengles.EXTDisjointTimerQuery;
import org.lwjgl.opengles.GLES30;
import org.lwjgl.opengles.GLESCapabilities;

public final class GLApi {
	public static GLESCapabilities glesCapabilities;

	private GLApi() {
	}

	public static final int GL_VERTEX_SHADER = GLES30.GL_VERTEX_SHADER;
	public static final int GL_FRAGMENT_SHADER = GLES30.GL_FRAGMENT_SHADER;
	public static final int GL_COMPILE_STATUS = GLES30.GL_COMPILE_STATUS;
	public static final int GL_LINK_STATUS = GLES30.GL_LINK_STATUS;
	public static final int GL_VALIDATE_STATUS = GLES30.GL_VALIDATE_STATUS;
	public static final int GL_TRUE = GLES30.GL_TRUE;
	public static final int GL_FALSE = GLES30.GL_FALSE;
	public static final int GL_NO_ERROR = GLES30.GL_NO_ERROR;
	public static final int GL_ARRAY_BUFFER = GLES30.GL_ARRAY_BUFFER;
	public static final int GL_STATIC_DRAW = GLES30.GL_STATIC_DRAW;
	public static final int GL_DYNAMIC_DRAW = GLES30.GL_DYNAMIC_DRAW;
	public static final int GL_STREAM_DRAW = GLES30.GL_STREAM_DRAW;
	public static final int GL_MAP_WRITE_BIT = GLES30.GL_MAP_WRITE_BIT;
	public static final int GL_MAP_INVALIDATE_BUFFER_BIT = GLES30.GL_MAP_INVALIDATE_BUFFER_BIT;
	public static final int GL_MAP_FLUSH_EXPLICIT_BIT = GLES30.GL_MAP_FLUSH_EXPLICIT_BIT;
	public static final int GL_UNIFORM_BUFFER = GLES30.GL_UNIFORM_BUFFER;
	public static final int GL_PIXEL_UNPACK_BUFFER = GLES30.GL_PIXEL_UNPACK_BUFFER;
	public static final int GL_WRITE_ONLY = 0x88B9; // not in LWJGL GLES30
	public static final int GL_RENDERER = GLES30.GL_RENDERER;
	public static final int GL_VERSION = GLES30.GL_VERSION;
	public static final int GL_FRAMEBUFFER = GLES30.GL_FRAMEBUFFER;
	public static final int GL_READ_FRAMEBUFFER = GLES30.GL_READ_FRAMEBUFFER;
	public static final int GL_DRAW_FRAMEBUFFER = GLES30.GL_DRAW_FRAMEBUFFER;
	public static final int GL_FRAMEBUFFER_COMPLETE = GLES30.GL_FRAMEBUFFER_COMPLETE;
	public static final int GL_COLOR_ATTACHMENT0 = GLES30.GL_COLOR_ATTACHMENT0;
	public static final int GL_DEPTH_ATTACHMENT = GLES30.GL_DEPTH_ATTACHMENT;
	public static final int GL_RENDERBUFFER = GLES30.GL_RENDERBUFFER;
	public static final int GL_RGBA = GLES30.GL_RGBA;
	public static final int GL_RGBA8 = GLES30.GL_RGBA8;
	public static final int GL_DEPTH_COMPONENT24 = GLES30.GL_DEPTH_COMPONENT24;
	public static final int GL_COLOR_BUFFER_BIT = GLES30.GL_COLOR_BUFFER_BIT;
	public static final int GL_DEPTH_BUFFER_BIT = GLES30.GL_DEPTH_BUFFER_BIT;
	public static final int GL_TRIANGLES = GLES30.GL_TRIANGLES;
	public static final int GL_TRIANGLE_FAN = GLES30.GL_TRIANGLE_FAN;
	public static final int GL_CULL_FACE = GLES30.GL_CULL_FACE;
	public static final int GL_BLEND = GLES30.GL_BLEND;
	public static final int GL_DEPTH_TEST = GLES30.GL_DEPTH_TEST;
	public static final int GL_MULTISAMPLE = 0x809D; // not in LWJGL GLES30
	public static final int GL_SRC_ALPHA = GLES30.GL_SRC_ALPHA;
	public static final int GL_ONE_MINUS_SRC_ALPHA = GLES30.GL_ONE_MINUS_SRC_ALPHA;
	public static final int GL_ONE = GLES30.GL_ONE;
	public static final int GL_GREATER = GLES30.GL_GREATER;
	public static final int GL_NEAREST = GLES30.GL_NEAREST;
	public static final int GL_LINEAR = GLES30.GL_LINEAR;
	public static final int GL_TEXTURE_2D = GLES30.GL_TEXTURE_2D;
	public static final int GL_TEXTURE_2D_ARRAY = GLES30.GL_TEXTURE_2D_ARRAY;
	public static final int GL_TEXTURE_MIN_FILTER = GLES30.GL_TEXTURE_MIN_FILTER;
	public static final int GL_TEXTURE_MAG_FILTER = GLES30.GL_TEXTURE_MAG_FILTER;
	public static final int GL_TEXTURE_WRAP_S = GLES30.GL_TEXTURE_WRAP_S;
	public static final int GL_TEXTURE_WRAP_T = GLES30.GL_TEXTURE_WRAP_T;
	public static final int GL_CLAMP_TO_EDGE = GLES30.GL_CLAMP_TO_EDGE;
	public static final int GL_NEAREST_MIPMAP_LINEAR = GLES30.GL_NEAREST_MIPMAP_LINEAR;
	public static final int GL_FLOAT = GLES30.GL_FLOAT;
	public static final int GL_INT = GLES30.GL_INT;
	public static final int GL_SHORT = GLES30.GL_SHORT;
	public static final int GL_UNSIGNED_BYTE = GLES30.GL_UNSIGNED_BYTE;
	public static final int GL_UNSIGNED_INT = GLES30.GL_UNSIGNED_INT;
	public static final int GL_UNSIGNED_INT_8_8_8_8_REV = 0x8367; // not in LWJGL GLES30
	public static final int GL_SAMPLES = GLES30.GL_SAMPLES;
	public static final int GL_MAX_SAMPLES = GLES30.GL_MAX_SAMPLES;
	public static final int GL_INVALID_ENUM = GLES30.GL_INVALID_ENUM;
	public static final int GL_INVALID_VALUE = GLES30.GL_INVALID_VALUE;
	public static final int GL_INVALID_OPERATION = GLES30.GL_INVALID_OPERATION;
	public static final int GL_INVALID_FRAMEBUFFER_OPERATION = GLES30.GL_INVALID_FRAMEBUFFER_OPERATION;
	public static final int GL_BACK = GLES30.GL_BACK;
	public static final int GL_DONT_CARE = GLES30.GL_DONT_CARE;
	public static final int GL_TEXTURE0 = GLES30.GL_TEXTURE0;
	public static final int GL_TEXTURE1 = GLES30.GL_TEXTURE1;
	public static final int GL_ELEMENT_ARRAY_BUFFER = GLES30.GL_ELEMENT_ARRAY_BUFFER;
	public static final int GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT = 0x84FF;
	public static final int GL_TEXTURE_MAX_ANISOTROPY_EXT = 0x84FE;
	public static final int GL_TEXTURE_SWIZZLE_R = 0x8E42;
	public static final int GL_TEXTURE_SWIZZLE_B = 0x8E44;
	public static final int GL_RED = GLES30.GL_RED;
	public static final int GL_BLUE = GLES30.GL_BLUE;

	public static int glCreateProgram() {
		return GLES30.glCreateProgram();
	}

	public static int glCreateShader(int type) {
		return GLES30.glCreateShader(type);
	}

	public static void glShaderSource(int shader, CharSequence source) {
		GLES30.glShaderSource(shader, source);
	}

	public static void glCompileShader(int shader) {
		GLES30.glCompileShader(shader);
	}

	public static int glGetShaderi(int shader, int pname) {
		return GLES30.glGetShaderi(shader, pname);
	}

	public static String glGetShaderInfoLog(int shader) {
		return GLES30.glGetShaderInfoLog(shader);
	}

	public static void glDeleteShader(int shader) {
		GLES30.glDeleteShader(shader);
	}

	public static void glAttachShader(int program, int shader) {
		GLES30.glAttachShader(program, shader);
	}

	public static void glLinkProgram(int program) {
		GLES30.glLinkProgram(program);
	}

	public static int glGetProgrami(int program, int pname) {
		return GLES30.glGetProgrami(program, pname);
	}

	public static String glGetProgramInfoLog(int program) {
		return GLES30.glGetProgramInfoLog(program);
	}

	public static void glValidateProgram(int program) {
		GLES30.glValidateProgram(program);
	}

	public static void glDetachShader(int program, int shader) {
		GLES30.glDetachShader(program, shader);
	}

	public static void glDeleteProgram(int program) {
		GLES30.glDeleteProgram(program);
	}

	public static void glUseProgram(int program) {
		GLES30.glUseProgram(program);
	}

	public static int glGetUniformLocation(int program, CharSequence name) {
		return GLES30.glGetUniformLocation(program, name);
	}

	public static int glGetUniformBlockIndex(int program, CharSequence name) {
		return GLES30.glGetUniformBlockIndex(program, name);
	}

	public static void glUniformBlockBinding(int program, int blockIndex, int blockBinding) {
		GLES30.glUniformBlockBinding(program, blockIndex, blockBinding);
	}

	public static void glUniform4i(int location, int v0, int v1, int v2, int v3) {
		GLES30.glUniform4i(location, v0, v1, v2, v3);
	}

	public static void glUniform2i(int location, int v0, int v1) {
		GLES30.glUniform2i(location, v0, v1);
	}

	public static void glUniform4f(int location, float v0, float v1, float v2, float v3) {
		GLES30.glUniform4f(location, v0, v1, v2, v3);
	}

	public static void glUniform1i(int location, int v0) {
		GLES30.glUniform1i(location, v0);
	}

	public static void glUniform1f(int location, float v0) {
		GLES30.glUniform1f(location, v0);
	}

	public static void glUniform3iv(int location, int[] value) {
		GLES30.glUniform3iv(location, value);
	}

	public static void glUniform2fv(int location, float[] value) {
		GLES30.glUniform2fv(location, value);
	}

	public static void glUniform3i(int location, int v0, int v1, int v2) {
		GLES30.glUniform3i(location, v0, v1, v2);
	}

	public static void glUniformMatrix4fv(int location, boolean transpose, float[] value) {
		GLES30.glUniformMatrix4fv(location, transpose, value);
	}

	public static void glMultiDrawArrays(int mode, IntBuffer first, IntBuffer count) {
		int n = first.remaining();
		int fPos = first.position();
		int cPos = count.position();
		for (int i = 0; i < n; i++) {
			int cnt = count.get(cPos + i);
			if (cnt > 0) {
				GLES30.glDrawArrays(mode, first.get(fPos + i), cnt);
			}
		}
	}

	// --- Buffers ---
	public static int glGenBuffers() {
		return GLES30.glGenBuffers();
	}

	public static void glDeleteBuffers(int buffer) {
		GLES30.glDeleteBuffers(buffer);
	}

	public static void glBindBuffer(int target, int buffer) {
		GLES30.glBindBuffer(target, buffer);
	}

	public static void glBufferData(int target, long size, int usage) {
		GLES30.glBufferData(target, size, usage);
	}

	public static void glBufferData(int target, FloatBuffer data, int usage) {
		GLES30.glBufferData(target, data, usage);
	}

	public static void glBufferData(int target, ByteBuffer data, int usage) {
		GLES30.glBufferData(target, data, usage);
	}

	public static void glBufferData(int target, IntBuffer data, int usage) {
		GLES30.glBufferData(target, data, usage);
	}

	public static void glBufferSubData(int target, long offset, FloatBuffer data) {
		GLES30.glBufferSubData(target, offset, data);
	}

	public static void glBufferSubData(int target, long offset, IntBuffer data) {
		GLES30.glBufferSubData(target, offset, data);
	}

	public static ByteBuffer glMapBufferRange(int target, long offset, long length, int access, ByteBuffer oldBuffer) {
		return GLES30.glMapBufferRange(target, offset, length, access, oldBuffer);
	}

	public static ByteBuffer glMapBuffer(int target, int access, long size) {
		return GLES30.glMapBufferRange(target, 0, size, access, (ByteBuffer) null);
	}

	public static boolean glUnmapBuffer(int target) {
		return GLES30.glUnmapBuffer(target);
	}

	public static void glFlushMappedBufferRange(int target, long offset, long length) {
		GLES30.glFlushMappedBufferRange(target, offset, length);
	}

	public static void glBindBufferBase(int target, int index, int buffer) {
		GLES30.glBindBufferBase(target, index, buffer);
	}

	public static int glGenVertexArrays() {
		return GLES30.glGenVertexArrays();
	}

	public static void glDeleteVertexArrays(int array) {
		GLES30.glDeleteVertexArrays(array);
	}

	public static void glBindVertexArray(int array) {
		GLES30.glBindVertexArray(array);
	}

	public static void glEnableVertexAttribArray(int index) {
		GLES30.glEnableVertexAttribArray(index);
	}

	public static void glVertexAttribPointer(int index, int size, int type, boolean normalized, int stride, long pointer) {
		GLES30.glVertexAttribPointer(index, size, type, normalized, stride, pointer);
	}

	public static void glVertexAttribIPointer(int index, int size, int type, int stride, long pointer) {
		GLES30.glVertexAttribIPointer(index, size, type, stride, pointer);
	}

	public static void glDrawArrays(int mode, int first, int count) {
		GLES30.glDrawArrays(mode, first, count);
	}

	public static void glDrawElements(int mode, int count, int type, long indices) {
		GLES30.glDrawElements(mode, count, type, indices);
	}

	public static void glViewport(int x, int y, int width, int height) {
		GLES30.glViewport(x, y, width, height);
	}

	public static void glClearColor(float r, float g, float b, float a) {
		GLES30.glClearColor(r, g, b, a);
	}

	public static void glClearDepth(double depth) {
		GLES30.glClearDepthf((float) depth);
	}

	public static void glClear(int mask) {
		GLES30.glClear(mask);
	}

	public static void glEnable(int cap) {
		GLES30.glEnable(cap);
	}

	public static void glDisable(int cap) {
		GLES30.glDisable(cap);
	}

	public static void glBlendFunc(int sfactor, int dfactor) {
		GLES30.glBlendFunc(sfactor, dfactor);
	}

	public static void glBlendFuncSeparate(int srcRGB, int dstRGB, int srcAlpha, int dstAlpha) {
		GLES30.glBlendFuncSeparate(srcRGB, dstRGB, srcAlpha, dstAlpha);
	}

	public static void glDepthFunc(int func) {
		GLES30.glDepthFunc(func);
	}

	public static void glDepthMask(boolean flag) {
		GLES30.glDepthMask(flag);
	}

	public static void glColorMask(boolean r, boolean g, boolean b, boolean a) {
		GLES30.glColorMask(r, g, b, a);
	}

	public static void glScissor(int x, int y, int width, int height) {
		GLES30.glScissor(x, y, width, height);
	}

	public static void glFinish() {
		GLES30.glFinish();
	}

	public static int glGenTextures() {
		return GLES30.glGenTextures();
	}

	public static void glDeleteTextures(int texture) {
		GLES30.glDeleteTextures(texture);
	}

	public static void glActiveTexture(int texture) {
		GLES30.glActiveTexture(texture);
	}

	public static void glBindTexture(int target, int texture) {
		GLES30.glBindTexture(target, texture);
	}

	public static void glTexParameteri(int target, int pname, int param) {
		GLES30.glTexParameteri(target, pname, param);
	}

	public static void glTexParameterf(int target, int pname, float param) {
		GLES30.glTexParameterf(target, pname, param);
	}

	public static void glTexImage2D(int target, int level, int internalFormat, int width, int height, int border, int format, int type, long pixels) {
		GLES30.glTexImage2D(target, level, internalFormat, width, height, border, format, type, pixels);
	}

	public static void glTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height, int format, int type, long pixels) {
		GLES30.glTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type, pixels);
	}

	public static void glTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height, int format, int type, ByteBuffer pixels) {
		GLES30.glTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type, pixels);
	}

	public static void glTexImage3D(int target, int level, int internalFormat, int width, int height, int depth, int border, int format, int type, long pixels) {
		GLES30.glTexImage3D(target, level, internalFormat, width, height, depth, border, format, type, pixels);
	}

	public static void glTexSubImage3D(int target, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, ByteBuffer pixels) {
		GLES30.glTexSubImage3D(target, level, xoffset, yoffset, zoffset, width, height, depth, format, type, pixels);
	}

	public static void glTexStorage3D(int target, int levels, int internalFormat, int width, int height, int depth) {
		GLES30.glTexStorage3D(target, levels, internalFormat, width, height, depth);
	}

	public static void glGenerateMipmap(int target) {
		GLES30.glGenerateMipmap(target);
	}

	public static int glGenFramebuffers() {
		return GLES30.glGenFramebuffers();
	}

	public static void glDeleteFramebuffers(int framebuffer) {
		GLES30.glDeleteFramebuffers(framebuffer);
	}

	public static void glBindFramebuffer(int target, int framebuffer) {
		GLES30.glBindFramebuffer(target, framebuffer);
	}

	public static void glFramebufferTexture2D(int target, int attachment, int textarget, int texture, int level) {
		GLES30.glFramebufferTexture2D(target, attachment, textarget, texture, level);
	}

	public static void glFramebufferRenderbuffer(int target, int attachment, int renderbuffertarget, int renderbuffer) {
		GLES30.glFramebufferRenderbuffer(target, attachment, renderbuffertarget, renderbuffer);
	}

	public static int glCheckFramebufferStatus(int target) {
		return GLES30.glCheckFramebufferStatus(target);
	}

	public static int glGenRenderbuffers() {
		return GLES30.glGenRenderbuffers();
	}

	public static void glDeleteRenderbuffers(int renderbuffer) {
		GLES30.glDeleteRenderbuffers(renderbuffer);
	}

	public static void glBindRenderbuffer(int target, int renderbuffer) {
		GLES30.glBindRenderbuffer(target, renderbuffer);
	}

	public static void glRenderbufferStorageMultisample(int target, int samples, int internalFormat, int width, int height) {
		GLES30.glRenderbufferStorageMultisample(target, samples, internalFormat, width, height);
	}

	public static void glBlitFramebuffer(int srcX0, int srcY0, int srcX1, int srcY1, int dstX0, int dstY0, int dstX1, int dstY1, int mask, int filter) {
		GLES30.glBlitFramebuffer(srcX0, srcY0, srcX1, srcY1, dstX0, dstY0, dstX1, dstY1, mask, filter);
	}

	public static void glReadBuffer(int mode) { /* GLES: no glReadBuffer for default FBO */ }

	public static void glReadPixels(int x, int y, int width, int height, int format, int type, ByteBuffer pixels) {
		GLES30.glReadPixels(x, y, width, height, format, type, pixels);
	}

	public static final int GL_TIMESTAMP = EXTDisjointTimerQuery.GL_TIMESTAMP_EXT;
	public static final int GL_QUERY_RESULT_AVAILABLE = EXTDisjointTimerQuery.GL_QUERY_RESULT_AVAILABLE_EXT;
	public static final int GL_QUERY_RESULT = EXTDisjointTimerQuery.GL_QUERY_RESULT_EXT;

	public static boolean hasGpuTimerQueries() {
		if (glesCapabilities == null || !glesCapabilities.GL_EXT_disjoint_timer_query)
			return false;
		// Function pointers can be null if extension is advertised but not implemented (e.g. some ANGLE configs)
		return glesCapabilities.glGetQueryObjectivEXT != 0 && glesCapabilities.glGetQueryObjectui64vEXT != 0;
	}

	public static void glGenQueries(int[] ids) {
		if (hasGpuTimerQueries())
			EXTDisjointTimerQuery.glGenQueriesEXT(ids);
	}

	public static void glDeleteQueries(int[] ids) {
		if (hasGpuTimerQueries())
			EXTDisjointTimerQuery.glDeleteQueriesEXT(ids);
	}

	public static void glQueryCounter(int id, int target) {
		if (hasGpuTimerQueries())
			EXTDisjointTimerQuery.glQueryCounterEXT(id, target);
	}

	public static void glGetQueryObjectiv(int id, int pname, int[] params) {
		if (hasGpuTimerQueries())
			EXTDisjointTimerQuery.glGetQueryObjectivEXT(id, pname, params);
	}

	public static long glGetQueryObjectui64(int id, int pname) {
		return hasGpuTimerQueries() ? EXTDisjointTimerQuery.glGetQueryObjectui64EXT(id, pname) : 0L;
	}


	// --- Query ---
	public static int glGetInteger(int pname) {
		return GLES30.glGetInteger(pname);
	}

	public static float glGetFloat(int pname) {
		return GLES30.glGetFloat(pname);
	}

	public static int glGetError() {
		return GLES30.glGetError();
	}

	public static String glGetString(int name) {
		return GLES30.glGetString(name);
	}

	public static boolean hasTexStorage3D() {
		return true;
	}

	public static boolean hasAnisotropicFiltering() {
		return glesCapabilities != null && glesCapabilities.GL_EXT_texture_filter_anisotropic;
	}
}
