package element;
import it.unisa.dia.gas.jpbc.PairingParametersGenerator;
import it.unisa.dia.gas.jpbc.PairingPreProcessing;
import it.unisa.dia.gas.plaf.jpbc.field.ElementPowPreProcessingTest;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.pairing.a.TypeACurveGenerator;
import it.unisa.dia.gas.plaf.jpbc.pairing.e.TypeECurveGenerator;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;

import javax.swing.plaf.synth.SynthSeparatorUI;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.ElementPowPreProcessing;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
public class Test {

	
	private static int rBits = 160;
	private static int qBits = 1024;
	public static void main(String[] args) {
		
		PairingParametersGenerator parametersGenerator = new TypeACurveGenerator(rBits, qBits);

		// Then, generate the parameters by invoking the generate method.
		PairingParameters params = parametersGenerator.generate();
		System.out.println(params.toString());
		FileWriter fw = null;
		try {
			fw = new FileWriter("params.propreties");
			fw.write(params.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		PairingFactory.getInstance().setUsePBCWhenPossible(true);
	
		Pairing pairing = PairingFactory.getPairing(params);
		
		Field Zr = pairing.getZr();
		Field G1 = pairing.getG1();
		Field G2 = pairing.getG2();
		Field GT = pairing.getGT();
		
		G1.getNqr();
		
		Element in1 = G1.newRandomElement();
		Element in2 = G2.newRandomElement();
		System.out.println("in1: " + in1.toString() + "\nin2: " + in2);
		
		Element in3 = pairing.pairing(in1, in2);
		System.out.println("in3: " + in3);
		
		
		Element in4 = G1.newRandomElement();
		Element in5 = G2.newRandomElement();
		System.out.println("in4: " + in4 + "\nin5: " + in5);
		
		BigInteger n = new BigInteger("3");
		System.out.println(n.toString());
		Element in6 = in4.getElementPowPreProcessing().pow(n);
		System.out.println("in6: " + in6);
		System.out.println(in4.isEqual(in6));
	
	}
}
