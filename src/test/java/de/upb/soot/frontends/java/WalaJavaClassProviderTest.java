package de.upb.soot.frontends.java;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import categories.Java8Test;
import de.upb.soot.frontends.ClassSource;
import de.upb.soot.inputlocation.FileType;
import de.upb.soot.inputlocation.JavaSourcePathAnalysisInputLocation;
import de.upb.soot.signatures.PackageName;
import de.upb.soot.types.JavaClassType;
import de.upb.soot.util.ImmutableUtils;
import java.nio.file.Paths;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(Java8Test.class)
public class WalaJavaClassProviderTest {

  @Test
  public void testCreateClassSource() {
    String srcDir = "src/test/resources/wala-tests/";
    String exclusionFilePath = srcDir + "WalaExclusions.txt";
    JavaSourcePathAnalysisInputLocation inputLocation =
        new JavaSourcePathAnalysisInputLocation(
            ImmutableUtils.immutableSet(srcDir), exclusionFilePath);
    JavaClassType type = new JavaClassType("Array1", PackageName.DEFAULT_PACKAGE);

    WalaJavaClassProvider provider = new WalaJavaClassProvider();
    ClassSource classSource = provider.createClassSource(inputLocation, Paths.get(srcDir), type);

    assertEquals(type, classSource.getClassType());

    ClassSource content = classSource;
    assertNotNull(content);
    assertEquals(3, content.resolveMethods().size());
    assertEquals(0, content.resolveFields().size());

    assertEquals(content, (classSource));
  }

  @Test
  public void testGetHandledFileType() {
    assertEquals(FileType.JAVA, new WalaJavaClassProvider().getHandledFileType());
  }
}
