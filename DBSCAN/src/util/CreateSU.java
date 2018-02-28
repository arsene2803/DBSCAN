package util;

import java.io.IOException;

public class CreateSU {
	public static void main(String[] args) throws IOException {
		SU su=new SU();
		double x_lb=Double.parseDouble(args[0]);
		double y_lb=Double.parseDouble(args[1]);
		double x_tr=Double.parseDouble(args[2]);
		double y_tr=Double.parseDouble(args[3]);
		double epsilon=Double.parseDouble(args[4]);
		String fileName=args[5];
		char delimiter=',';
		su.createSU(x_lb, y_lb, x_tr, y_tr, epsilon, fileName, delimiter);
	}

}
