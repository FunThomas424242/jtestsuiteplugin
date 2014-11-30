package gh.funthomas424242.junitsupport.processors;

import gh.funthomas424242.junitsupport.annotations.TestCategories;
import gh.funthomas424242.junitsupport.annotations.TestSuite;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic.Kind;
import javax.tools.JavaFileObject;

//@SupportedAnnotationTypes(value = { "gh.funthomas424242.junitsupport.annotations.TestSuite" })
@SupportedAnnotationTypes(value = { "*" })
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class TestSuiteProcessor extends AbstractProcessor {

	private static final String GENERATED_BASE_TYPE = "GeneratedTestSuite";

	private final static Logger LOG = Logger.getLogger(TestSuiteProcessor.class
			.getName());

	private Messager messager;
	private Filer filer;
	private Map<String, Set<String>> categoryMap = new HashMap<String, Set<String>>();

	public TestSuiteProcessor() {
		super();
	}

	@Override
	public void init(final ProcessingEnvironment processingEnv) {
		this.messager = processingEnv.getMessager();
		this.filer = processingEnv.getFiler();
	}

	/**
	 * simple write out the annotations of TypeElement
	 */
	@Override
	public boolean process(final Set<? extends TypeElement> annotations,
			final RoundEnvironment roundEnv) {

		if (!roundEnv.processingOver()) {

			// initialize Category Map
			final Set<? extends Element> annotatedElements = roundEnv
					.getElementsAnnotatedWith(TestCategories.class);
			for (Element annotatedElement : annotatedElements) {
				final TestCategories classAnnotation = annotatedElement
						.getAnnotation(TestCategories.class);
				if (classAnnotation != null) {
					final String className = annotatedElement.asType()
							.toString() + ".class";
					final String[] categoryNames = classAnnotation.names();

					for (int i = 0; categoryNames != null
							&& i < categoryNames.length; i++) {
						if (this.categoryMap.containsKey(categoryNames[i])) {
							final Set<String> classNameSet = this.categoryMap
									.get(categoryNames[i]);
							classNameSet.add(className);
							// only needed for HashSet
							// because the hash has changed
							// this.categoryMap.put(categoryNames[i],
							// classNameSet);
						} else {
							final Set<String> classNameSet = new HashSet<String>();
							classNameSet.add(className);
							this.categoryMap
									.put(categoryNames[i], classNameSet);
						}
					}
				}
			}

			final Set<String> categories = this.categoryMap.keySet();
			for (String category : categories) {
				final StringBuffer buf = new StringBuffer();
				buf.append("[" + category + "]:(");
				final Set<String> classNames = this.categoryMap.get(category);
				for (String className : classNames) {
					buf.append(className);
					buf.append(",");
				}
				buf.append(")");
				LOG.info(buf.toString());
			}

			for (Element e : roundEnv.getRootElements()) {

				TypeElement typDecl = findEnclosingTypeElement(e);
				System.out.printf("\nScanning Type %s\n\n",
						typDecl.getQualifiedName());

				TestSuite typAnnotation = typDecl
						.getAnnotation(TestSuite.class);
				computeAnnotation(typDecl, typAnnotation);

				for (Element ee : typDecl.getEnclosedElements()) {

					TestSuite annotation = ee.getAnnotation(TestSuite.class);
					computeAnnotation(typDecl, annotation);
				}
			}
		}

		// if (!roundEnv.processingOver()) {
		//
		// for (TypeElement annotation : annotations) {
		//
		// final Element typDecl = annotation.getEnclosingElement();
		// System.out.printf("\nScanning Type %s\n\n",
		// typDecl.getSimpleName());
		//
		// for (ExecutableElement ee : ElementFilter.methodsIn(typDecl
		// .getEnclosedElements())) {
		//
		// TestSuite myanno = ee.getAnnotation(TestSuite.class);
		//
		// System.out.printf("%s Name value = %s\n",
		// ee.getSimpleName(),
		// myanno == null ? null : myanno.name());
		// }
		// }
		// }

		//
		// // logInfo("AnnotationProcessor entry to process");
		// // System.out.println("begin processing");
		// for (final TypeElement annotation : annotations) {
		// LOG.info("verarbeite annotation: " + annotation.getSimpleName());
		// LOG.info("annotation name: " + annotation.getQualifiedName());
		// LOG.info("TestSuite name: " + TestSuite.class.getName());
		//
		// final String annotationType = annotation.getQualifiedName()
		// .toString();
		// if (annotationType.equals(TestSuite.class.getName())) {
		// LOG.info("ANNO GEFUNDEN !!!!!!!");
		// try {
		// Element delcTyp= annotation.getEnclosingElement();
		// AnnotationMirror mirror = delcTyp.getAnnotationMirrors();
		//
		//
		//
		// LOG.info("Cast erfolgreich");
		// } catch (Exception ex) {
		// LOG.info(ex.toString());
		// }
		// }
		// }

		// Set<? extends Element> elements = roundEnv.getRootElements();
		//
		// for (Element element : elements) {
		// LOG.info("Begin Element: " + element.getSimpleName());
		//
		// List<? extends AnnotationMirror> mirrors = element
		// .getAnnotationMirrors();
		// for (AnnotationMirror mirror : mirrors) {
		//
		// LOG.info("Anno Mirror: " + mirror.getAnnotationType());
		// if (mirror.getAnnotationType().toString()
		// .equals(TestSuite.class.getName())) {
		//
		// LOG.info("ANNO GEFUNDEN !!!!!!!");
		// try {
		// final Map<? extends ExecutableElement, ? extends AnnotationValue>
		// valueMap = mirror
		// .getElementValues();
		// final Set<?> valueSet=valueMap.entrySet();
		//
		// final Map<String, AnnotationValue> parameterMap = new HashMap<String,
		// AnnotationValue>();
		//
		// for( Map.Entry<? extends ExecutableElement, ? extends
		// AnnotationValue> entry : mirror.getElementValues().entrySet() ) {
		// if( “value”.equals( entry.getKey().getSimpleName().toString() ) ) {
		// action = entry.getValue();
		// break;
		// }
		// }
		// for (final Object annoValue : valueSet) {
		// LOG.info("annoValue: "+annoValue.toString());
		// // final String key = annoValue.getKey().getSimpleName();
		// // final AnnotationValue value = annoValue
		// // .getValue();
		// // parameterMap.put(key, value);
		// }
		// } catch (Exception ex) {
		// LOG.info(ex.toString());
		// }
		//
		// }
		// }
		// }

		// if (annoTyp instanceof TestSuite) {
		// LOG.info("ANNO Type is TestSuite ");
		// }
		//
		// if
		// (annotation.getQualifiedName().equals(TestSuite.class.getName()))
		// {
		// final TestSuite anno = (TestSuite) annotation;
		// final String[] kategorien = anno.categories();
		// for (int i = 0; i < kategorien.length; i++) {
		// LOG.info("kategorie: " + kategorien[i]);
		// }
		// }
		//

		// final Set<? extends Element> elements = roundEnv
		// .getElementsAnnotatedWith(annotation);
		//
		// for (final Element element : elements) {
		// LOG.info("verarbeite element: " + element.getSimpleName()
		// + " annotiert mit "
		// + annotation.getTypeParameters().toString());
		// writeFile(element);
		// }
		// }

		return true;
	}

	private void computeAnnotation(TypeElement typDecl, TestSuite annotation) {
		if (annotation != null) {
			final String packageName = annotation.packageName();
			final String className = annotation.name();
			final String[] categories = annotation.categories();
			final Set<String> testClassNames = new HashSet<String>();
			for (int i = 0; categories != null && i < categories.length; i++) {
				final Set<String> tmpNames = this.categoryMap
						.get(categories[i]);
				if (tmpNames != null) {
					testClassNames.addAll(tmpNames);
				}
			}
			writeFile(typDecl, packageName, className, testClassNames);
		}
	}

	public TypeElement findEnclosingTypeElement(Element e) {
		while (e != null && !(e instanceof TypeElement)) {
			// only annotation types goes here
			e = e.getEnclosingElement();
		}
		return TypeElement.class.cast(e);
	}

	private void logInfo(final String message) {
		LOG.info(message);
	}

	private void writeFile(final Element element, final String packageName,
			final String className, final Set<String> testClassNames) {

		try {

			// final String fileName =
			// getGeneratedFileName(GENERATED_BASE_PACKAGE);
			// final String className = getGeneratedClassName();
			final String fileName = packageName + "." + className;
			final JavaFileObject fo = filer.createSourceFile(fileName, element);
			final Writer w = fo.openWriter();
			w.append(getCode(packageName, className, testClassNames));
			w.flush();
			w.close();
		} catch (final IOException ioe) {
			ioe.printStackTrace();
			messager.printMessage(Kind.WARNING, ioe.toString(), element);
		}
	}

	protected String getCode(final String packageName,
			final String classifierName, final Set<String> testClassNames) {
		final StringBuffer buf = new StringBuffer();
		buf.append("package " + packageName + ";\n\n");
		buf.append("import org.junit.runner.RunWith;\n");
		buf.append("import org.junit.runners.Suite;\n");
		buf.append("import org.junit.runners.Suite.SuiteClasses;\n\n");
		buf.append("@RunWith(Suite.class)\n");
		buf.append("@SuiteClasses({ ");
		boolean isFirstRun = true;
		for (Iterator<String> iterator = testClassNames.iterator(); iterator
				.hasNext();) {
			if (isFirstRun) {
				isFirstRun = false;
			} else {
				buf.append(",");
			}
			buf.append(iterator.next());
		}
		// TestCaseTest1.class, TestClass.class
		buf.append("})\n");
		buf.append("public class " + classifierName + " {\n\n" + "}\n");
		return buf.toString();
	}

	private String getGeneratedFileName(final String packageName) {
		return packageName + "." + getGeneratedClassName();
	}

	private String getGeneratedClassName() {
		return GENERATED_BASE_TYPE;
	}

}
