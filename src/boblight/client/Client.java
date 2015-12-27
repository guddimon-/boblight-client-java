package boblight.client;

import java.awt.Dialog.ModalExclusionType;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.text.MaskFormatter;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import boblight.client.actions.ColorWheel1;
import boblight.client.actions.ColorWheel2;
import boblight.client.actions.ColorWheel3;
import boblight.client.actions.GreenFlash;
import boblight.client.actions.ScreenCapture;
import boblight.client.actions.SmoothColors1;
import boblight.client.actions.SmoothColors2;
import boblight.client.actions.dialogs.ColorChooser;
import boblight.client.actions.dialogs.CustomColors;
import boblight.types.Boblight;

public class Client {
	/*
	 * Create a logger
	 */
	private final static Logger log = Logger.getLogger(Client.class);
	
	private String host = "192.168.2.101";
	private Integer port = 19333;
	private Integer speed = 100;
	private Integer priority = 128;

	private JFrame frmBoblight;
	private JTextField inHost;
	private JTextField inPort;
	private JTextField inColor;
	private JTextField inSpeed;
	private JTextField inPriority;
	
	private GreenFlash threadGreenFlash = null;
	private SmoothColors1 threadSmoothColors1 = null;
	private SmoothColors2 threadSmoothColors2 = null;
	private ColorWheel1 threadColorWheel1 = null;
	private ColorWheel2 threadColorWheel2 = null;
	private ColorWheel3 threadColorWheel3 = null;
	private ScreenCapture threadScreenCapture = null;

	/**
	 * Launch the application.
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException, IOException {
		
//		Properties props = new Properties();
//		props.load(new FileInputStream("./config/log4j.properties"));
//		PropertyConfigurator.configure(props);

		PropertyConfigurator.configure("./config/log4j.properties");
		log.info("Client started");
		
		if (args.length != 0) {
			CustomColors customColors;
			customColors = new CustomColors("192.168.2.101", 19333);
			customColors.show();
		}
		else {
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						Client window = new Client();
						window.frmBoblight.setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
	}

	/**
	 * Create the application.
	 * @throws ParseException 
	 * @wbp.parser.entryPoint
	 */
	public Client() throws ParseException {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws ParseException 
	 */
	private void initialize() throws ParseException {
		frmBoblight = new JFrame();
		frmBoblight.setTitle("Boblight");
		frmBoblight.setBounds(100, 100, 600, 512);
		frmBoblight.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmBoblight.getContentPane().setLayout(null);
		frmBoblight.setModalExclusionType(ModalExclusionType.NO_EXCLUDE);

		JButton btnGreenFlash = new JButton(
				"<html><center>Gr\u00FCner<br />Blitz</center></html>");
		btnGreenFlash.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				interruptThread();
				try {
					threadGreenFlash = new GreenFlash(inHost.getText(), new Integer(inPort.getText()));
					threadGreenFlash.start();
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnGreenFlash.setBounds(66, 143, 136, 74);
		frmBoblight.getContentPane().add(btnGreenFlash);

		JButton btnSmoothColors1 = new JButton(
				"<html><center>ruhiger<br />Farbwechsel 1</center></html>");
		btnSmoothColors1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				interruptThread();
				try {
					threadSmoothColors1 = new SmoothColors1(inHost.getText(), new Integer(inPort.getText()));
					threadSmoothColors1.start();
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnSmoothColors1.setBounds(235, 143, 136, 74);
		frmBoblight.getContentPane().add(btnSmoothColors1);

		JButton btnSmoothColors2 = new JButton(
				"<html><center>ruhiger<br />Farbwechsel 2</center></html>");
		btnSmoothColors2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				interruptThread();
				try {
					threadSmoothColors2 = new SmoothColors2(inHost.getText(), new Integer(inPort.getText()));
					threadSmoothColors2.start();
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnSmoothColors2.setBounds(402, 143, 136, 74);
		frmBoblight.getContentPane().add(btnSmoothColors2);

		JButton btnColorChooser = new JButton(
				"<html><center>manuelle<br />Farbauswahl</center></html>");
		btnColorChooser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				interruptThread();
				try {
					ColorChooser cc;
					cc = new ColorChooser(inHost.getText(), new Integer(inPort.getText()));
					cc.show();
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnColorChooser.setBounds(66, 253, 136, 74);
		frmBoblight.getContentPane().add(btnColorChooser);

		JLabel lblHost = new JLabel("Host");
		lblHost.setBounds(66, 16, 56, 16);
		frmBoblight.getContentPane().add(lblHost);

		JLabel lblPort = new JLabel("Port");
		lblPort.setBounds(298, 16, 56, 16);
		frmBoblight.getContentPane().add(lblPort);

		inHost = new JTextField(host);
		inHost.setBounds(134, 13, 116, 22);
		frmBoblight.getContentPane().add(inHost);
		inHost.setColumns(10);

		MaskFormatter mfPort = new MaskFormatter("#####");
		inPort = new JFormattedTextField(mfPort);
		inPort.setText(port.toString());
		inPort.setBounds(366, 13, 116, 22);
		frmBoblight.getContentPane().add(inPort);
		inPort.setColumns(10);

		inColor = new JTextField();
		inColor.setBounds(366, 45, 116, 22);
		frmBoblight.getContentPane().add(inColor);
		inColor.setColumns(10);

		JLabel lblFarbe = new JLabel("Farbe");
		lblFarbe.setBounds(298, 48, 56, 16);
		frmBoblight.getContentPane().add(lblFarbe);

		JButton btnMachWat = new JButton("mach wat");
		btnMachWat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Boblight b = new Boblight();
				try {
					b.connect(inHost.getText(), new Integer(inPort.getText()));
					b.setSpeed(new Double(inSpeed.getText()));
					b.setPriority(new Integer(inPriority.getText()));
					b.setColor(inColor.getText());
					// b.disconnect();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnMachWat.setBounds(336, 80, 97, 25);
		frmBoblight.getContentPane().add(btnMachWat);

		JLabel lblGeschwindigkeit = new JLabel("Speed");
		lblGeschwindigkeit.setBounds(66, 48, 56, 16);
		frmBoblight.getContentPane().add(lblGeschwindigkeit);

		MaskFormatter mfSpeed = new MaskFormatter("###");
		inSpeed = new JFormattedTextField(mfSpeed);
		inSpeed.setText(speed.toString());
		inSpeed.setColumns(10);
		inSpeed.setBounds(134, 45, 116, 22);
		frmBoblight.getContentPane().add(inSpeed);

		JLabel lblPriority = new JLabel("Priority");
		lblPriority.setBounds(66, 82, 56, 16);
		frmBoblight.getContentPane().add(lblPriority);

		MaskFormatter mfPriority = new MaskFormatter("###");
		inPriority = new JFormattedTextField(mfPriority);
		inPriority.setText(priority.toString());
		inPriority.setColumns(10);
		inPriority.setBounds(134, 79, 116, 22);
		frmBoblight.getContentPane().add(inPriority);

		JButton btnColorWheel = new JButton(
				"<html><center>Farbrad 1</center></html>");
		btnColorWheel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				interruptThread();
				try {
					threadColorWheel1 = new ColorWheel1(inHost.getText(), new Integer(inPort.getText()));
					threadColorWheel1.start();
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnColorWheel.setBounds(235, 253, 136, 74);
		frmBoblight.getContentPane().add(btnColorWheel);

		JButton button = new JButton("<html><center>Farbrad 2</center></html>");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				interruptThread();
				try {
					threadColorWheel2 = new ColorWheel2(inHost.getText(), new Integer(inPort.getText()));
					threadColorWheel2.start();
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

//				try {
//					ColorSwirl.makeColorGradient(0.9, 0.9, 0.9, 0, 4, 8);
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
			}
		});
		button.setBounds(402, 253, 136, 74);
		frmBoblight.getContentPane().add(button);

		JButton button_1 = new JButton(
				"<html><center>Bildschirmrand</center></html>");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				interruptThread();
				try {
					threadScreenCapture = new ScreenCapture(inHost.getText(), new Integer(inPort.getText()));
					threadScreenCapture.start();
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		button_1.setBounds(235, 362, 136, 74);
		frmBoblight.getContentPane().add(button_1);
		
		JButton button_2 = new JButton("<html><center>Farbrad 3</center></html>");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				interruptThread();
				try {
					threadColorWheel3 = new ColorWheel3(inHost.getText(), new Integer(inPort.getText()));
					threadColorWheel3.start();
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		button_2.setBounds(66, 362, 136, 74);
		frmBoblight.getContentPane().add(button_2);
		
		JButton button_3 = new JButton("stooop");
		button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				interruptThread();
			}
		});
		button_3.setBounds(441, 80, 97, 25);
		frmBoblight.getContentPane().add(button_3);
		
		JButton button_4 = new JButton("<html><center>eigene Farbwahl<br />pro LED<br />(Farbverlauf)</center></html>");
		button_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				interruptThread();
				try {
					CustomColors customColors;
					customColors = new CustomColors(inHost.getText(), new Integer(inPort.getText()));
					customColors.show();
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		button_4.setBounds(402, 362, 136, 74);
		frmBoblight.getContentPane().add(button_4);
	}

	protected void interruptThread() {
		if (threadGreenFlash != null)
			threadGreenFlash.interrupt();
		if (threadSmoothColors1 != null)
			threadSmoothColors1.interrupt();
		if (threadSmoothColors2 != null)
			threadSmoothColors2.interrupt();
		if (threadColorWheel1 != null)
			threadColorWheel1.interrupt();
		if (threadColorWheel2 != null)
			threadColorWheel2.interrupt();
		if (threadColorWheel3 != null)
			threadColorWheel3.interrupt();
		if (threadScreenCapture != null)
			threadScreenCapture.interrupt();
	}
}
