package gh.funthomas424242.junitsupport.processors;

import java.io.IOException;
import java.io.Writer;
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
import javax.tools.Diagnostic.Kind;
import javax.tools.JavaFileObject;

@SupportedAnnotationTypes(value = { "*" })
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class TestSuiteProcessor extends AbstractProcessor {

	private static final String GENERATED_BASE_PACKAGE = "gh.funthomas424242.testsuites";
	private static final String GENERATED_BASE_TYPE = "GeneratedTestSuite";

	private final static Logger LOG = Logger.getLogger(TestSuiteProcessor.class
			.getName());

	private Messager messager;
	private Filer filer;

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

		// logInfo("AnnotationProcessor entry to process");
		System.out.println("begin processing");
		for (final TypeElement annotation : annotations) {
			final Set<? extends Element> elements = roundEnv
					.getElementsAnnotatedWith(annotation);

			for (final Element element : elements) {
				writeFile(element);
			}
		}

		return true;
	}

	private void logInfo(final String message) {
		LOG.info(message);
	}

	private void writeFile(final Element element) {

		try {

			final String fileName = getGeneratedFileName(GENERATED_BASE_PACKAGE);
			final String className = getGeneratedClassName();

			final JavaFileObject fo = filer.createSourceFile(fileName, element);
			final Writer w = fo.openWriter();
			w.append(getCode(GENERATED_BASE_PACKAGE, className));
			w.flush();
			w.close();
		} catch (final IOException ioe) {
			ioe.printStackTrace();
			messager.printMessage(Kind.WARNING, ioe.toString(), element);
		}
	}

	protected String getCode(final String packageName, final String typeName) {

		return "package test"
				 + packageName
				+ ";\n\npublic class  "
				 + typeName
				+ " { \n\n"
				+ "  final private Integer value; \n\n"
				+ "  public "
				+ typeName
				+ " (final int zahl) { \n"
				+ "   if ((zahl % 2) == 0) { \n"
				+ "     throw new IllegalArgumentException(\"Value\" + zahl + \" is not odd.\"); \n   } \n"
				+ "   value = zahl;\n" + "}  \n"
				+ "public Integer getValue() {\n" + "  return value;  \n"
				+ "}\n" + "}\n";

	}

	private String getGeneratedFileName(final String packageName) {
		return packageName + "." + getGeneratedClassName();
	}

	private String getGeneratedClassName() {
		return GENERATED_BASE_TYPE;
	}

}
