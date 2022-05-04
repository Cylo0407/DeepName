package abbrivatiate_expander.src.Global;

import abbrivatiate_expander.src.Step0.PreOperation;
import abbrivatiate_expander.src.Step1.ExtractAST;
import abbrivatiate_expander.src.Step2.HandleCSV;

import java.io.File;
import java.io.IOException;

public class Game
{
	public static void main(String[] args) throws IOException
	{
		if (args.length == 1)
		{
			LX.javaSource = args[0];
			LX.javaDest = args[0] + "_Expand.java";
		} else if (args.length == 2)
		{
			LX.javaSource = args[0];
			LX.javaDest = args[1];
		} else
		{
			System.out.println("please input souceCodeLocation[souceCodeDestion]");
		}
		if (!CheckLegalInput())
		{
			System.out.println("Error input");
			return;
		}
		LX.tempFile = LX.javaSource + "_temp.csv";

		PreOperation.main(null);
		ExtractAST.main(null);
		HandleCSV.main(null);
	}

	public static boolean CheckLegalInput()
	{
		if (new File(LX.javaSource).exists()
				&& (!new File(LX.javaDest).exists() || LX.javaDest.equals(LX.javaSource)))
		{
			return true;
		}
		return false;
	}

}
