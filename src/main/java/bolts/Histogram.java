package src.main.java.storm.bolts;

//For Chart
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;


public class Histogram {

    private static final int BAR_WIDTH = 50;
    private static final int INCREMENT = 1;

    public Histogram(Map<String, Integer> counters) {
	
        final JTextArea textArea = new JTextArea(5, 40);
		final Map<String, Integer> counts = counters;
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        JButton go = new JButton("Histogram the data !");
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(textArea, BorderLayout.CENTER);
        panel.add(go, BorderLayout.SOUTH);

        JFrame frame = new JFrame();
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
		
		//Plotting graph dynamically based on the entries in hasmap. This will make sure that we show the bar chart in a good way
        go.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String text = textArea.getText();
                int width = counts.size() * BAR_WIDTH + 150;
                int max = maxCount(counts);
                int height = (max * INCREMENT)/30;
                int horizon = height - 80;
                HistogramPanel panel = new HistogramPanel(width, counts, height, horizon);
                JOptionPane.showMessageDialog(null, panel);
            }
        });
    }
	
	// This gets the Max Count in the hashmap (In this program, since we get DESC hashmap as an input, we can actually take top/1st key's value. But this was written keeping generic case in mind, where we need to find the max in a hashmap)
    private int maxCount(Map<String, Integer> counts) {
        int max = 0;
        for (Integer num : counts.values()) {
            if (num > max) {
                max = num;
            }
        }
        return max;
    }

    public class HistogramPanel extends JPanel {

        int width;
        int dimHeight;
        int horizon;
        Map<String, Integer> counts;

        public HistogramPanel(int width, Map<String, Integer> counts, int dimHeight, int horizon) {
            this.width = width;
            this.counts = counts;
            this.dimHeight = dimHeight;
            this.horizon = horizon;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int x = 10,xconst=10;
			int ycoor = 0;
			int counter=1;
            for (Map.Entry<String, Integer> entry : counts.entrySet()) {
                int height = (entry.getValue() * INCREMENT)/100;
                int y = horizon - height;
                g.fillRect(x, y, BAR_WIDTH - 10, height);
				g.drawString(Integer.toString(counter)+" ", x, horizon + 10);
                g.drawString(Integer.toString(counter)+") "+entry.getKey(), xconst, (horizon + 20)-ycoor);
				g.drawString(entry.getValue() + " ", x, y -2);
                x += BAR_WIDTH;
				ycoor -= 11;
				counter++;
            }
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(width, dimHeight);
        }
    }
}