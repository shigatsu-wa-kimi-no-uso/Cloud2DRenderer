package me.project.cloud2drenderer;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import me.project.cloud2drenderer.renderer.procedure.binding.glresource.shader.AttributeBindingProcessor;
import me.project.cloud2drenderer.renderer.context.CommonRenderContext;
import me.project.cloud2drenderer.renderer.context.RenderContext;
import me.project.cloud2drenderer.renderer.procedure.binding.glresource.shader.UniformBindingProcessor;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {


    @Test
    public void testAnnotation() throws IllegalAccessException {
        RenderContext context = new CommonRenderContext();
        UniformBindingProcessor processor = new UniformBindingProcessor();

    }

    @Test
    public void testModel(){

    }

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("me.project.cloud2drenderer", appContext.getPackageName());
    }
}