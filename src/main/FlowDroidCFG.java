package main;

import common.Constants;
import dotgraph.NodeVisitor;
import soot.Body;
import soot.Modifier;
import soot.PackManager;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.jimple.infoflow.android.SetupApplication;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.options.Options;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.UnitGraph;
import soot.util.cfgcmd.CFGToDotGraph;
import soot.util.dot.DotGraph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import soot.toolkits.graph.pdg.HashMutablePDG;

public class FlowDroidCFG {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String fileAndroidJar = Constants.ANDROID_JAR.toAbsolutePath().toString();
        String fileApk = Constants.APK_FABRICA_WORDCARD.toAbsolutePath().toString();
        
        System.out.println(fileApk);
        String[] splitApk = fileApk.split("\\\\");
        String nameApk = splitApk[splitApk.length - 1].replace(".apk", "");
//        System.out.println(nameApk);
        
        soot.G.reset();

        SetupApplication app = new SetupApplication(fileAndroidJar, fileApk);
//        app.setCallbackFile(callBack);

        Options.v().set_src_prec(Options.src_prec_apk);
        Options.v().set_process_dir(Collections.singletonList(fileApk));
        Options.v().set_force_android_jar(fileAndroidJar);
        Options.v().set_whole_program(true);
        Options.v().set_allow_phantom_refs(true);
        Options.v().set_output_format(Options.output_format_none);
        Options.v().setPhaseOption("cg.spark", "verbose:true");

        PackManager.v().runPacks();

        app.constructCallgraph();
        Scene.v().loadNecessaryClasses();

        SootMethod entryPoint = app.getDummyMainMethod();

        Options.v().set_main_class(entryPoint.getSignature());

        Scene.v().setEntryPoints(Collections.singletonList(entryPoint));

        CallGraph cg = Scene.v().getCallGraph();
        // boolean filterAndroidSystemCLass = false;

        DotGraph dot = new DotGraph("callgraph");
//        System.out.println(DotGraph.DOT_EXTENSION);

        NodeVisitor.visit(dot, cg, entryPoint);
        //
        dot.plot(Constants.OUTPUT_FOLDER.resolve("CallGraph_" + nameApk +
                DotGraph.DOT_EXTENSION).toAbsolutePath().toString());


        // Test
        Iterator<Edge> Edges = cg.iterator();
        ArrayList<String> Nodes = new ArrayList<String>();
        int count = 0;
        while (Edges.hasNext()) {

            Edge edge = Edges.next();
            SootMethod sourceMethod = edge.src();
//            String sourceIndentifier = getIdentifier(sourceMethod);
            SootMethod descMethod = edge.tgt();
//            String descIndentifier = getIdentifier(descMethod);
            System.out.println(sourceMethod.getSignature());            
            System.out.println(edge.getSrc() + " " + edge.getTgt());            
//            System.out.println("Name Source:" + sourceMethod.getName() + " " + "Name Desc:" + descMethod.getName());
//            System.out.println(sourceIndentifier + " " + descIndentifier);
            Nodes.add(sourceMethod.getSignature());
            Nodes.add(descMethod.getSignature());
//            System.out.println("Active Body: " + sourceMethod.getActiveBody());

//            Body body = sourceMethod.retrieveActiveBody();
//            UnitGraph cfg = new ExceptionalUnitGraph(body);
//
//            CFGToDotGraph cfgtodot = new CFGToDotGraph();
//            
////            DotGraph dg = cfgtodot.drawCFG((ExceptionalUnitGraph) cfg);
////
////            dg.plot(Constants.OUTPUT_FOLDER.resolve("CFGMethod" +
////                    DotGraph.DOT_EXTENSION).toAbsolutePath().toString());
//
////            System.exit(1);
//            HashMutablePDG hpdg=new HashMutablePDG(cfg);
//            DotGraph pdg=cfgtodot.drawCFG(hpdg,body);
//
//            pdg.plot(Constants.OUTPUT_FOLDER.resolve("PDGMethodJar" +
//                    DotGraph.DOT_EXTENSION).toAbsolutePath().toString());
            System.out.println(count);
            count++;
        }
        System.out.println(Nodes.size());
        ArrayList<String> UniqueNodes = new ArrayList<>(new HashSet<>(Nodes));
        System.out.println(UniqueNodes.size());
	}
	
	public static String getIdentifier(SootMethod sootMethod) {
		String identifier;
		
		if (0 == sootMethod.getDeclaringClass().getInterfaceCount()) {
			identifier = "<NoInterface>";
		} else {
			identifier = "<Interfaces:";
			for (SootClass sootClass: sootMethod.getDeclaringClass().getInterfaces()) {
				identifier += " " + sootClass;
			}
			identifier += ">";
		}
		
		if (false == sootMethod.getDeclaringClass().hasSuperclass()) {
			identifier += " <NoSuperClass>";
		} else {
			identifier += " <SuperClass: " + sootMethod.getDeclaringClass().getSuperclass().toString() + ">";
		}

		if (Modifier.toString(sootMethod.getModifiers()).equals("")) {
			identifier += " <NoModifier>";
		} else {
			identifier += " <Modifiers: " + Modifier.toString(sootMethod.getModifiers()) + ">";
		}
		
		identifier += " " + sootMethod.getSignature();		
		return identifier;
	}
}
