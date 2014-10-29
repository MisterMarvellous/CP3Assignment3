public class MandelProcessListing extends javax.swing.JPanel {
    private String processName;
    private int threadsNo;
    private javax.swing.JLabel processNameLabel;
    private javax.swing.JProgressBar[] progressBars;
    private javax.swing.JButton cancelButton;
    private java.awt.GridBagLayout layout;
    private java.awt.GridBagConstraints c;

    public MandelProcessListing(String name, int threadsNo) {
	processName = name;
	this.threadsNo = threadsNo;
	processNameLabel = new javax.swing.JLabel();
	progressBars = new javax.swing.JProgressBar[threadsNo];
	cancelButton = new javax.swing.JButton();
	layout = new java.awt.GridBagLayout();
	c = new java.awt.GridBagConstraints();

	setBorder(javax.swing.BorderFactory.createRaisedBevelBorder());
	
	processNameLabel.setText(processName);

	setLayout(layout);
	c.gridwidth = 2;
	c.anchor = java.awt.GridBagConstraints.LINE_START;
	c.insets = new java.awt.Insets(3, 7, 3, 3);
	add(processNameLabel, c);


	c.insets = new java.awt.Insets(3, 3, 3, 3);
	c.gridwidth = 1;
	c.fill = java.awt.GridBagConstraints.BOTH;
	c.weightx = 1.0;
	for (int i = 0; i < progressBars.length; i++) {
	    progressBars[i] = new javax.swing.JProgressBar();
	    c.gridy = i+1;
	    add(progressBars[i], c);
	}

	c.gridx = 1;
	c.gridheight = 2;
	c.fill = java.awt.GridBagConstraints.NONE;
	c.anchor = java.awt.GridBagConstraints.LINE_END;
	cancelButton.setText("Cancel");
	add(cancelButton, c);
	
    }

    public void setBarProgress(int progress, int bar) {
	if (bar < progressBars.length) {
	    progressBars[bar].setValue(progress);
	}
    }
}