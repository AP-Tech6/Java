import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NumberAnalyzer {
    // The main method starts the Java program.
    public static void main(String[] args) {
        // Try to set a nicer look and feel if it is available.
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // If it fails, the default look is still fine.
        }

        // Create the graphical user interface on the event thread.
        SwingUtilities.invokeLater(NumberAnalyzer::createAndShowUI);
    }

    // Build and show the window with all components.
    private static void createAndShowUI() {
        JFrame frame = new JFrame("Beginner Number Analyzer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(520, 460);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        // Root panel with a custom background.
        JPanel root = new GradientPanel();
        root.setLayout(new BorderLayout(20, 20));
        root.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title label shows the purpose of the app.
        JLabel title = new JLabel("Number Analyzer");
        title.setFont(new Font("SansSerif", Font.BOLD, 26));
        title.setForeground(Color.WHITE);
        title.setHorizontalAlignment(SwingConstants.CENTER);

        // Instruction text below the title.
        JLabel description = new JLabel("Enter an integer and click Analyze to see its properties.");
        description.setFont(new Font("SansSerif", Font.PLAIN, 14));
        description.setForeground(new Color(220, 220, 220));
        description.setHorizontalAlignment(SwingConstants.CENTER);

        // Input panel holds the text field and button.
        JPanel inputPanel = new JPanel();
        inputPanel.setOpaque(false);
        inputPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JLabel numberLabel = new JLabel("Number:");
        numberLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        numberLabel.setForeground(Color.WHITE);

        JTextField numberField = new JTextField(16);
        numberField.setFont(new Font("SansSerif", Font.PLAIN, 16));

        JButton analyzeButton = new JButton("Analyze");
        analyzeButton.setFont(new Font("SansSerif", Font.BOLD, 15));

        inputPanel.add(numberLabel);
        inputPanel.add(numberField);
        inputPanel.add(analyzeButton);

        // Result label shows the analysis output.
        JLabel resultLabel = new JLabel();
        resultLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        resultLabel.setForeground(Color.WHITE);
        resultLabel.setVerticalAlignment(SwingConstants.TOP);

        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.setOpaque(false);
        resultPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255, 120)),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)));
        resultPanel.add(resultLabel, BorderLayout.CENTER);

        // Add all parts to the root panel.
        root.add(title, BorderLayout.NORTH);
        root.add(description, BorderLayout.CENTER);
        root.add(inputPanel, BorderLayout.SOUTH);

        // Use a panel to keep the result separate and clear.
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setOpaque(false);
        centerPanel.add(description, BorderLayout.NORTH);
        centerPanel.add(inputPanel, BorderLayout.CENTER);
        centerPanel.add(resultPanel, BorderLayout.SOUTH);

        root.add(title, BorderLayout.NORTH);
        root.add(centerPanel, BorderLayout.CENTER);

        frame.setContentPane(root);

        // Handle click events when the button is pressed.
        analyzeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = numberField.getText().trim();

                // Check if the user entered anything.
                if (text.isEmpty()) {
                    resultLabel.setText("Please type an integer value.");
                    return;
                }

                try {
                    long number = Long.parseLong(text);
                    String result = analyzeNumber(number);
                    resultLabel.setText(formatHtml(result));
                } catch (NumberFormatException ex) {
                    resultLabel.setText("Invalid input. Please enter a whole number.");
                }
            }
        });

        frame.setVisible(true);
    }

    // Create a readable HTML result string from the analysis.
    private static String formatHtml(String text) {
        return "<html><body style='font-family:SansSerif; color:#F0F8FF;'>" + text + "</body></html>";
    }

    // Analyze the number and return a description of its properties.
    private static String analyzeNumber(long number) {
        String evenOdd = (number % 2 == 0) ? "Even" : "Odd";
        String sign = (number > 0) ? "Positive" : (number < 0) ? "Negative" : "Zero";
        String primeComposite = classifyPrimeComposite(number);
        String armstrong = isArmstrong(number) ? "Yes" : "No";
        String palindrome = isPalindrome(number) ? "Yes" : "No";
        String perfect = isPerfect(number) ? "Yes" : "No";
        String magical = isMagical(number) ? "Yes" : "No";
        String amicable = findAmicablePair(number);

        // Build a simple multi-line result string.
        return "<b>Number:</b> " + number + "<br>"
                + "<b>Even / Odd:</b> " + evenOdd + "<br>"
                + "<b>Positive / Negative:</b> " + sign + "<br>"
                + "<b>Prime / Composite:</b> " + primeComposite + "<br>"
                + "<b>Armstrong:</b> " + armstrong + "<br>"
                + "<b>Palindrome:</b> " + palindrome + "<br>"
                + "<b>Perfect:</b> " + perfect + "<br>"
                + "<b>Magical:</b> " + magical + "<br>"
                + "<b>Amicable:</b> " + amicable;
    }

    // Return whether the number is prime, composite, or neither.
    private static String classifyPrimeComposite(long number) {
        if (number <= 1) {
            return "Neither prime nor composite";
        }
        if (isPrime(number)) {
            return "Prime";
        }
        return "Composite";
    }

    // Check if a number is prime.
    private static boolean isPrime(long number) {
        if (number <= 1) {
            return false;
        }
        if (number <= 3) {
            return true;
        }
        if (number % 2 == 0 || number % 3 == 0) {
            return false;
        }
        long limit = (long) Math.sqrt(number);
        for (long i = 5; i <= limit; i += 6) {
            if (number % i == 0 || number % (i + 2) == 0) {
                return false;
            }
        }
        return true;
    }

    // Check if a number is an Armstrong number.
    private static boolean isArmstrong(long number) {
        long value = Math.abs(number);
        String digits = String.valueOf(value);
        long sum = 0;

        for (char c : digits.toCharArray()) {
            int digit = c - '0';
            sum += Math.pow(digit, digits.length());
        }

        return sum == value;
    }

    // Check if a number reads the same forwards and backwards.
    private static boolean isPalindrome(long number) {
        long value = Math.abs(number);
        long reversed = 0;
        long temp = value;

        while (temp > 0) {
            reversed = reversed * 10 + (temp % 10);
            temp /= 10;
        }

        return reversed == value;
    }

    // Check if the number is perfect.
    private static boolean isPerfect(long number) {
        if (number <= 1) {
            return false;
        }
        return sumOfProperDivisors(number) == number;
    }

    // A magical number is one whose digits eventually reduce to 1.
    private static boolean isMagical(long number) {
        long value = Math.abs(number);
        while (value > 9) {
            value = digitSum(value);
        }
        return value == 1;
    }

    // Sum of digits helper.
    private static long digitSum(long number) {
        long sum = 0;
        while (number > 0) {
            sum += number % 10;
            number /= 10;
        }
        return sum;
    }

    // Find an amicable pair if one exists.
    private static String findAmicablePair(long number) {
        if (number <= 0) {
            return "Not applicable";
        }

        long friend = sumOfProperDivisors(number);
        if (friend == number || friend <= 0) {
            return "No amicable pair found";
        }
        if (sumOfProperDivisors(friend) == friend && sumOfProperDivisors(friend) == number) {
            return "Yes, pair with " + friend;
        }
        return "No amicable pair found";
    }

    // Sum of all proper divisors of a number.
    private static long sumOfProperDivisors(long number) {
        if (number <= 1) {
            return 0;
        }
        long sum = 1;
        long limit = (long) Math.sqrt(number);
        for (long i = 2; i <= limit; i++) {
            if (number % i == 0) {
                sum += i;
                long other = number / i;
                if (other != i) {
                    sum += other;
                }
            }
        }
        return sum;
    }

    // Custom panel draws a smooth gradient background.
    private static class GradientPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            int width = getWidth();
            int height = getHeight();
            GradientPaint paint = new GradientPaint(0, 0, new Color(8, 32, 72), width, height, new Color(12, 88, 152));
            g2.setPaint(paint);
            g2.fillRect(0, 0, width, height);
            g2.dispose();
        }
    }
}
