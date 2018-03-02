import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Painter;

public class Arithmatic {
	
 public static class Data {
		public char Char;
		public double counter=0;
		
		
	Data(){}

}

 public	static class Ranges{
		public double low;
		public double high;
		
	public Ranges() {}	
		
	}
	
public static void getRanges(String in ,HashMap<Character,Ranges>HM){
	
		
		//char[] input = in.toCharArray();
		ArrayList<Data>Al=new ArrayList<>();
		
		for(int i=0;i<in.length();i++){
			
			boolean check=false;
			
			for(int j=0;j<Al.size();j++){
			
				if(in.charAt(i)==Al.get(j).Char){
					Al.get(j).counter++;
					check = true;
				}
				
			}
			if(check==false){
				Data d=new Data();
				d.Char=in.charAt(i);
				d.counter=1;
				Al.add(d);
			}
			
		}

		//Check the counters...
for(int i=0;i<Al.size();i++)
System.out.println("Char---> "+Al.get(i).Char+" Counter---> "+Al.get(i).counter);
System.out.println("-----------------------------------------------------------");

//generate Probabilities..
for(int i=0;i<Al.size();i++)
	Al.get(i).counter=Al.get(i).counter/in.length();


for(int i=0;i<Al.size();i++)
System.out.println("Char---> "+Al.get(i).Char+" Counter---> "+Al.get(i).counter);
System.out.println("-----------------------------------------------------------");

//generate Cummulative..
for(int i=0;i<Al.size();i++)
{
	if(i!=0)
	 Al.get(i).counter=Al.get(i).counter+Al.get(i-1).counter;
}

for(int i=0;i<Al.size();i++)
System.out.println("Char---> "+Al.get(i).Char+" Counter---> "+Al.get(i).counter);
System.out.println("-----------------------------------------------------------");


//Generate Ranges For each Character

for(int i=0;i<Al.size();i++){
	Ranges R=new Ranges();
	if(i==0)
	{	R.low=0;
		R.high=Al.get(i).counter;
	//	System.out.println(R.low+" "+R.high);
		HM.put(Al.get(i).Char, R);
	}
	else{
		R.low=Al.get(i-1).counter;
		R.high=Al.get(i).counter;
		//System.out.println(R.low+" "+R.high);
		HM.put(Al.get(i).Char, R);
	}
}

//for (Entry<Character, Ranges> entry : HM.entrySet()) {
//	    System.out.println(entry.getKey() + " --->low " + entry.getValue().low+"--->high "+entry.getValue().high);
//}



}

public static void Compress(String in) throws IOException{
	
	FileOutputStream f1 = new FileOutputStream("compress.txt");
	DataOutputStream bw = new DataOutputStream(f1);
	
	HashMap<Character,Ranges>HM=new HashMap<Character,Ranges>();
	getRanges(in,HM);
	
//Testing Ranges of each character...
	for (Entry<Character, Ranges> entry : HM.entrySet()) {
	    System.out.println(entry.getKey() + " --->low " + entry.getValue().low+"--->high "+entry.getValue().high);
}
System.out.println("------------------------------------------------------------");

double low=0 , high=1 , Range=1;
for(int i = 0 ; i<in.length();i++){
	
	double high_Symbol= HM.get(in.charAt(i)).high;
	double low_Symbol= HM.get(in.charAt(i)).low;
	
	high = low + Range*high_Symbol; 
	low = low + Range*low_Symbol;
	
	Range=high-low;
}
	
System.out.println(low+" "+high);

double x=low;
bw.writeDouble(x);
bw.writeInt(in.length());;
for (Entry<Character, Ranges> entry : HM.entrySet()) {
	bw.writeByte(entry.getKey());
	bw.writeDouble(entry.getValue().low);
	bw.writeDouble(entry.getValue().high);

}
	
	
}

public static void Decompress() throws IOException{
	
	FileInputStream file1 = new FileInputStream("compress.txt");
	DataInputStream data1 = new DataInputStream(file1);
	
	FileOutputStream f1 = new FileOutputStream("decompress.txt");
	DataOutputStream bw = new DataOutputStream(f1);
		
	HashMap<Character,Ranges>HM=new HashMap<Character,Ranges>();
	double Value=data1.readDouble();
	int size=data1.readInt();
	
	System.out.println(Value);
	
	while(data1.available()!=0){
		char x;
		double l,h;
		x=(char) data1.readByte();
		l=data1.readDouble();
		h=data1.readDouble();
		
		Ranges R=new Ranges();
		R.low=l;
		R.high=h;
		HM.put(x,R);
		
	}

	//Testing Ranges of each character...
		for (Entry<Character, Ranges> entry : HM.entrySet()) {
		    System.out.println(entry.getKey() + " --->low " + entry.getValue().low+"--->high "+entry.getValue().high);
	}
	System.out.println("------------------------------------------------------------");
	
	double code=Value;
	double low=0,high=1,Range=1;
	for(int i=0;i<size;i++){
			
			double low_symbol=0.0;
			double high_symbol=0.0;
		
			for (Entry<Character, Ranges> entry : HM.entrySet()){
				
				if(entry.getValue().low <= code && code <= entry.getValue().high)
				{
					
				 low_symbol=entry.getValue().low;
				 high_symbol=entry.getValue().high;
				 
				System.out.print(entry.getKey());
				bw.writeByte(entry.getKey());
				
				break;
				}
			
			
			}
			
			high=low+Range*high_symbol;
			low=low+Range*low_symbol;
			
			
			Range=high-low;
			code=(Value-low)/Range;
		
			}	
	
}
//-----------------------------------GUI---------------------------------//

private static JFrame mainFrame;
private static JLabel headerLabel;
private static JLabel statusLabel;
private static JPanel controlPanel;

private static void prepareGUI() {
	mainFrame = new JFrame("Java Swing Examples");
	mainFrame.setSize(400, 400);
	mainFrame.setLayout(new GridLayout(3, 1));
	mainFrame.addWindowListener(new WindowAdapter() {
		public void windowClosing(WindowEvent windowEvent) {
			System.exit(0);
		}
	});
	
	headerLabel = new JLabel("", JLabel.CENTER);
	statusLabel = new JLabel("", JLabel.CENTER);

	statusLabel.setSize(350, 100);

	controlPanel = new JPanel();
	controlPanel.setLayout(new FlowLayout());

	mainFrame.add(headerLabel);
	mainFrame.add(controlPanel);
	mainFrame.add(statusLabel);
	mainFrame.setVisible(true);
}

private static void showTextFieldDemo() {
	// headerLabel.setText("Control in action: JTextField");

	JLabel input = new JLabel("Input: ", JLabel.RIGHT);
	final JTextField userText = new JTextField(20);

	JButton Compress = new JButton("Compress");
	Compress.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			try {
				Compress(userText.getText());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	});
	JButton Decompress = new JButton("Decompress");
	Decompress.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			try {
				Decompress();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	});

	controlPanel.add(userText);
	controlPanel.add(Compress);
	controlPanel.add(Decompress);
	mainFrame.setVisible(true);
}



	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		//Compress("Bill Gates");	
		//Decompress("Bill Gates");
		prepareGUI();
		showTextFieldDemo();
		
		
	}

}













