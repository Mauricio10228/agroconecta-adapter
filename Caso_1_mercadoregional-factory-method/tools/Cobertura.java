import org.jacoco.core.analysis.*;
import org.jacoco.core.tools.ExecFileLoader;
import java.io.File;

/** Imprime cobertura de líneas y ramas del código de producción a partir de jacoco.exec. */
public class Cobertura {
    public static void main(String[] args) throws Exception {
        ExecFileLoader loader = new ExecFileLoader();
        loader.load(new File(args[0]));
        CoverageBuilder cb = new CoverageBuilder();
        Analyzer an = new Analyzer(loader.getExecutionDataStore(), cb);
        an.analyzeAll(new File(args[1]));
        for (IClassCoverage c : cb.getClasses()) {
            System.out.printf("  %-34s lineas %2d/%2d  ramas %2d/%2d%n", c.getName(),
                    c.getLineCounter().getCoveredCount(), c.getLineCounter().getTotalCount(),
                    c.getBranchCounter().getCoveredCount(), c.getBranchCounter().getTotalCount());
        }
        ICounter l = cb.getBundle("p").getLineCounter(), b = cb.getBundle("p").getBranchCounter();
        System.out.printf("COBERTURA lineas %d de %d  ramas %d de %d%n",
                l.getCoveredCount(), l.getTotalCount(), b.getCoveredCount(), b.getTotalCount());
    }
}
