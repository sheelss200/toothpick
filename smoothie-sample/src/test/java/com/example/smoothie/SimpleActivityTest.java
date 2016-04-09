package com.example.smoothie;

import android.app.Application;
import com.example.smoothie.deps.ContextNamer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.util.ActivityController;
import toothpick.ToothPick;
import toothpick.config.Module;
import toothpick.smoothie.module.DefaultActivityModule;
import toothpick.smoothie.module.DefaultApplicationModule;

import static com.google.common.truth.Truth.assertThat;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

@RunWith(RobolectricTestRunner.class) //
@Config(manifest = "src/main/AndroidManifest.xml")
public class SimpleActivityTest {

  @Test
  public void verifyInjectionAtOnCreate() {
    //GIVEN
    final ContextNamer mockContextNamer = createMock(ContextNamer.class);
    expect(mockContextNamer.getApplicationName()).andReturn("foo");
    expect(mockContextNamer.getActivityName()).andReturn("bar");
    ActivityController<SimpleActivity> controllerSimpleActivity = Robolectric.buildActivity(SimpleActivity.class);
    SimpleActivity activity = controllerSimpleActivity.get();
    ToothPick.createInjector(activity,
        new DefaultApplicationModule(Robolectric.application),
        new DefaultActivityModule(activity),
        new TestModule(mockContextNamer));
    replay(mockContextNamer);

    //WHEN
    controllerSimpleActivity.create();

    //THEN
    assertThat(activity.title.getText()).isEqualTo("foo");
    assertThat(activity.subTitle.getText()).isEqualTo("bar");
    verify(mockContextNamer);
  }

  private class TestModule extends Module {
    public TestModule(ContextNamer mockContextNamer) {
      bind(ContextNamer.class).to(mockContextNamer);
    }
  }
}