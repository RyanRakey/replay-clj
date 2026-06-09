package play_clj.test;

import com.badlogic.gdx.graphics.GL20;
import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.concurrent.atomic.AtomicInteger;

public class MockGL20 implements GL20 {
    private final AtomicInteger nextId = new AtomicInteger(1);
    private int nextHandle() { return nextId.getAndIncrement(); }

    // Critical: must return valid handles
    @Override public int glCreateShader(int type) { return nextHandle(); }
    @Override public int glCreateProgram() { return nextHandle(); }
    @Override public int glGenTexture() { return nextHandle(); }
    @Override public int glGenBuffer() { return nextHandle(); }
    @Override public int glGenFramebuffer() { return nextHandle(); }
    @Override public int glGenRenderbuffer() { return nextHandle(); }

    // Critical: must write success to output buffers
    @Override public void glGetShaderiv(int shader, int pname, IntBuffer params) { params.put(0, 1); }
    @Override public void glGetProgramiv(int program, int pname, IntBuffer params) { params.put(0, 1); }

    // Critical: must return >= 0
    @Override public int glGetUniformLocation(int program, String name) { return 0; }
    @Override public int glGetAttribLocation(int program, String name) { return 0; }

    // Critical: must return GL_NO_ERROR
    @Override public int glGetError() { return GL20.GL_NO_ERROR; }

    // Critical: must fill buffers with non-zero handles
    @Override public void glGenTextures(int n, IntBuffer t) { for (int i = 0; i < n; i++) t.put(i, nextHandle()); }
    @Override public void glGenBuffers(int n, IntBuffer b) { for (int i = 0; i < n; i++) b.put(i, nextHandle()); }
    @Override public void glGenFramebuffers(int n, IntBuffer b) { for (int i = 0; i < n; i++) b.put(i, nextHandle()); }
    @Override public void glGenRenderbuffers(int n, IntBuffer b) { for (int i = 0; i < n; i++) b.put(i, nextHandle()); }

    // Framebuffer status
    @Override public int glCheckFramebufferStatus(int target) { return GL20.GL_FRAMEBUFFER_COMPLETE; }

    // String/info queries
    @Override public String glGetShaderInfoLog(int shader) { return ""; }
    @Override public String glGetProgramInfoLog(int program) { return ""; }
    @Override public String glGetString(int name) { return ""; }
    @Override public String glGetActiveAttrib(int program, int index, IntBuffer size, IntBuffer type) { return ""; }
    @Override public String glGetActiveUniform(int program, int index, IntBuffer size, IntBuffer type) { return ""; }

    // Boolean queries
    @Override public boolean glIsShader(int shader) { return true; }
    @Override public boolean glIsProgram(int program) { return true; }
    @Override public boolean glIsTexture(int texture) { return true; }
    @Override public boolean glIsFramebuffer(int fb) { return true; }
    @Override public boolean glIsRenderbuffer(int rb) { return true; }
    @Override public boolean glIsBuffer(int buffer) { return true; }
    @Override public boolean glIsEnabled(int cap) { return false; }

    // All remaining methods are no-ops
    @Override public void glShaderSource(int shader, String source) {}
    @Override public void glCompileShader(int shader) {}
    @Override public void glAttachShader(int program, int shader) {}
    @Override public void glDetachShader(int program, int shader) {}
    @Override public void glLinkProgram(int program) {}
    @Override public void glUseProgram(int program) {}
    @Override public void glDeleteShader(int shader) {}
    @Override public void glDeleteProgram(int program) {}
    @Override public void glValidateProgram(int program) {}
    @Override public void glUniform1i(int loc, int v) {}
    @Override public void glUniform1f(int loc, float v) {}
    @Override public void glUniform2f(int loc, float a, float b) {}
    @Override public void glUniform2i(int loc, int a, int b) {}
    @Override public void glUniform3f(int loc, float a, float b, float c) {}
    @Override public void glUniform3i(int loc, int a, int b, int c) {}
    @Override public void glUniform4f(int loc, float a, float b, float c, float d) {}
    @Override public void glUniform4i(int loc, int a, int b, int c, int d) {}
    @Override public void glUniform1iv(int loc, int cnt, IntBuffer v) {}
    @Override public void glUniform1iv(int loc, int cnt, int[] v, int off) {}
    @Override public void glUniform2iv(int loc, int cnt, IntBuffer v) {}
    @Override public void glUniform2iv(int loc, int cnt, int[] v, int off) {}
    @Override public void glUniform3iv(int loc, int cnt, IntBuffer v) {}
    @Override public void glUniform3iv(int loc, int cnt, int[] v, int off) {}
    @Override public void glUniform4iv(int loc, int cnt, IntBuffer v) {}
    @Override public void glUniform4iv(int loc, int cnt, int[] v, int off) {}
    @Override public void glUniform1fv(int loc, int cnt, FloatBuffer v) {}
    @Override public void glUniform1fv(int loc, int cnt, float[] v, int off) {}
    @Override public void glUniform2fv(int loc, int cnt, FloatBuffer v) {}
    @Override public void glUniform2fv(int loc, int cnt, float[] v, int off) {}
    @Override public void glUniform3fv(int loc, int cnt, FloatBuffer v) {}
    @Override public void glUniform3fv(int loc, int cnt, float[] v, int off) {}
    @Override public void glUniform4fv(int loc, int cnt, FloatBuffer v) {}
    @Override public void glUniform4fv(int loc, int cnt, float[] v, int off) {}
    @Override public void glUniformMatrix2fv(int loc, int cnt, boolean tp, FloatBuffer v) {}
    @Override public void glUniformMatrix2fv(int loc, int cnt, boolean tp, float[] v, int off) {}
    @Override public void glUniformMatrix3fv(int loc, int cnt, boolean tp, FloatBuffer v) {}
    @Override public void glUniformMatrix3fv(int loc, int cnt, boolean tp, float[] v, int off) {}
    @Override public void glUniformMatrix4fv(int loc, int cnt, boolean tp, FloatBuffer v) {}
    @Override public void glUniformMatrix4fv(int loc, int cnt, boolean tp, float[] v, int off) {}
    @Override public void glVertexAttribPointer(int idx, int sz, int type, boolean norm, int stride, Buffer ptr) {}
    @Override public void glVertexAttribPointer(int idx, int sz, int type, boolean norm, int stride, int off) {}
    @Override public void glEnableVertexAttribArray(int idx) {}
    @Override public void glDisableVertexAttribArray(int idx) {}
    @Override public void glVertexAttrib1f(int idx, float x) {}
    @Override public void glVertexAttrib1fv(int idx, FloatBuffer v) {}
    @Override public void glVertexAttrib2f(int idx, float x, float y) {}
    @Override public void glVertexAttrib2fv(int idx, FloatBuffer v) {}
    @Override public void glVertexAttrib3f(int idx, float x, float y, float z) {}
    @Override public void glVertexAttrib3fv(int idx, FloatBuffer v) {}
    @Override public void glVertexAttrib4f(int idx, float x, float y, float z, float w) {}
    @Override public void glVertexAttrib4fv(int idx, FloatBuffer v) {}
    @Override public void glBindBuffer(int target, int buffer) {}
    @Override public void glBufferData(int target, int size, Buffer data, int usage) {}
    @Override public void glBufferSubData(int target, int offset, int size, Buffer data) {}
    @Override public void glDeleteBuffer(int buffer) {}
    @Override public void glDeleteBuffers(int n, IntBuffer buffers) {}
    @Override public void glActiveTexture(int texture) {}
    @Override public void glBindTexture(int target, int texture) {}
    @Override public void glDeleteTexture(int texture) {}
    @Override public void glDeleteTextures(int n, IntBuffer textures) {}
    @Override public void glTexImage2D(int t, int l, int ifmt, int w, int h, int b, int fmt, int type, Buffer px) {}
    @Override public void glTexSubImage2D(int t, int l, int x, int y, int w, int h, int fmt, int type, Buffer px) {}
    @Override public void glTexParameterf(int target, int pname, float param) {}
    @Override public void glTexParameteri(int target, int pname, int param) {}
    @Override public void glTexParameterfv(int target, int pname, FloatBuffer params) {}
    @Override public void glTexParameteriv(int target, int pname, IntBuffer params) {}
    @Override public void glCompressedTexImage2D(int t, int l, int ifmt, int w, int h, int b, int sz, Buffer data) {}
    @Override public void glCompressedTexSubImage2D(int t, int l, int xo, int yo, int w, int h, int fmt, int sz, Buffer data) {}
    @Override public void glCopyTexImage2D(int t, int l, int ifmt, int x, int y, int w, int h, int b) {}
    @Override public void glCopyTexSubImage2D(int t, int l, int xo, int yo, int x, int y, int w, int h) {}
    @Override public void glDrawArrays(int mode, int first, int count) {}
    @Override public void glDrawElements(int mode, int count, int type, Buffer indices) {}
    @Override public void glDrawElements(int mode, int count, int type, int offset) {}
    @Override public void glEnable(int cap) {}
    @Override public void glDisable(int cap) {}
    @Override public void glBlendFunc(int sfactor, int dfactor) {}
    @Override public void glBlendFuncSeparate(int sRGB, int dRGB, int sA, int dA) {}
    @Override public void glBlendEquation(int mode) {}
    @Override public void glBlendEquationSeparate(int mRGB, int mA) {}
    @Override public void glBlendColor(float r, float g, float b, float a) {}
    @Override public void glDepthMask(boolean flag) {}
    @Override public void glDepthFunc(int func) {}
    @Override public void glDepthRangef(float near, float far) {}
    @Override public void glCullFace(int mode) {}
    @Override public void glFrontFace(int mode) {}
    @Override public void glLineWidth(float width) {}
    @Override public void glViewport(int x, int y, int w, int h) {}
    @Override public void glScissor(int x, int y, int w, int h) {}
    @Override public void glClearColor(float r, float g, float b, float a) {}
    @Override public void glClear(int mask) {}
    @Override public void glClearDepthf(float depth) {}
    @Override public void glClearStencil(int s) {}
    @Override public void glColorMask(boolean r, boolean g, boolean b, boolean a) {}
    @Override public void glStencilFunc(int func, int ref, int mask) {}
    @Override public void glStencilMask(int mask) {}
    @Override public void glStencilOp(int fail, int zfail, int zpass) {}
    @Override public void glStencilFuncSeparate(int face, int func, int ref, int mask) {}
    @Override public void glStencilMaskSeparate(int face, int mask) {}
    @Override public void glStencilOpSeparate(int face, int fail, int zfail, int zpass) {}
    @Override public void glBindFramebuffer(int target, int fb) {}
    @Override public void glDeleteFramebuffer(int fb) {}
    @Override public void glDeleteFramebuffers(int n, IntBuffer fbs) {}
    @Override public void glFramebufferTexture2D(int t, int att, int tt, int tex, int lvl) {}
    @Override public void glFramebufferRenderbuffer(int t, int att, int rbt, int rb) {}
    @Override public void glBindRenderbuffer(int target, int rb) {}
    @Override public void glDeleteRenderbuffer(int rb) {}
    @Override public void glDeleteRenderbuffers(int n, IntBuffer rbs) {}
    @Override public void glRenderbufferStorage(int t, int ifmt, int w, int h) {}
    @Override public void glGenerateMipmap(int target) {}
    @Override public void glFinish() {}
    @Override public void glFlush() {}
    @Override public void glHint(int target, int mode) {}
    @Override public void glPixelStorei(int pname, int param) {}
    @Override public void glPolygonOffset(float factor, float units) {}
    @Override public void glSampleCoverage(float value, boolean invert) {}
    @Override public void glReadPixels(int x, int y, int w, int h, int fmt, int type, Buffer px) {}
    @Override public void glGetIntegerv(int pname, IntBuffer params) {
        // Return valid values for common queries
        // GL_MAX_TEXTURE_IMAGE_UNITS = 0x8872, GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS = 0x8B4D
        if (pname == 0x8872 || pname == 0x8B4D) {
            params.put(0, 16); // 16 texture units
        } else {
            params.put(0, 0);
        }
    }
    @Override public void glGetFloatv(int pname, FloatBuffer params) {}
    @Override public void glGetBooleanv(int pname, Buffer params) {}
    @Override public void glGetBufferParameteriv(int target, int pname, IntBuffer params) {}
    @Override public void glGetTexParameterfv(int target, int pname, FloatBuffer params) {}
    @Override public void glGetTexParameteriv(int target, int pname, IntBuffer params) {}
    @Override public void glGetUniformfv(int program, int loc, FloatBuffer params) {}
    @Override public void glGetUniformiv(int program, int loc, IntBuffer params) {}
    @Override public void glGetVertexAttribfv(int idx, int pname, FloatBuffer params) {}
    @Override public void glGetVertexAttribiv(int idx, int pname, IntBuffer params) {}
    @Override public void glGetVertexAttribPointerv(int idx, int pname, Buffer pointer) {}
    @Override public void glGetAttachedShaders(int program, int max, Buffer count, IntBuffer shaders) {}
    @Override public void glGetShaderPrecisionFormat(int shadertype, int precisionformat, IntBuffer range, IntBuffer precision) {}
    @Override public void glGetRenderbufferParameteriv(int target, int pname, IntBuffer params) {}
    @Override public void glGetFramebufferAttachmentParameteriv(int target, int attachment, int pname, IntBuffer params) {}
    @Override public void glReleaseShaderCompiler() {}
    @Override public void glShaderBinary(int n, IntBuffer shaders, int binaryformat, Buffer binary, int length) {}
    @Override public void glBindAttribLocation(int program, int index, String name) {}
}
