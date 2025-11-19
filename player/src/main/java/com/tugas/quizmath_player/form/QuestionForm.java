package form;

import repository.QuestionRepository;
import entity.Question;
import entity.OptionAnswer;
import helper.Session;
import java.awt.Image;
import java.util.Collections;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import repository.AnswerRepository;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author syafiq
 */
public class QuestionForm extends javax.swing.JFrame {
    private QuestionRepository question_repo;
    private AnswerRepository answer_repo;
    private List<Integer> randomizedIds;
    private int currentIndex = 0;
    private Question currentQuestion;
    private Timer globalTimer;
    private int totalTimeLeft; // dalam detik

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(QuestionForm.class.getName());

    /**
     * Creates new form QuestionForm
     */
   public QuestionForm() {
    initComponents();
    this.question_repo = new QuestionRepository();
    this.answer_repo = new AnswerRepository();
    setLocationRelativeTo(null);
    setExtendedState(JFrame.MAXIMIZED_BOTH);
    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

    // Load data di background
    new SwingWorker<List<Integer>, Void>() {
        @Override
        protected List<Integer> doInBackground() throws Exception {
            return question_repo.getAllId().getIds();
        }

        @Override
        protected void done() {
            try {
                randomizedIds = get();
                if (randomizedIds == null || randomizedIds.isEmpty()) {
                    JOptionPane.showMessageDialog(QuestionForm.this, "Tidak ada soal.");
                    return;
                }
                Collections.shuffle(randomizedIds);
                startGlobalTimer(100);
                loadQuestion();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }.execute();
}


    private void loadQuestion() {
        if (currentIndex < 0 || currentIndex >= randomizedIds.size()) {
            globalTimer.stop();
            new form.DoneForm().setVisible(true);
            this.dispose();
            return;
        }
        int number = currentIndex + 1;
        noSoal.setText(Integer.toString(number));

        
        int questionId = randomizedIds.get(currentIndex);
        this.currentQuestion = question_repo.getQuestionById(questionId);
         int siswaId = Session.getCurrentSiswaId();
        
  

        if (currentQuestion != null) {
        questionTextArea.setText(currentQuestion.question_text);
        levelLabel.setText("Level : " + currentQuestion.level);

       if (currentQuestion.image_path == null || currentQuestion.image_path.equals("")) {
            questionImgLabel.setIcon(null);  // hapus gambar
            questionImgLabel.setText("");    // hapus text
        } else {
            // load gambar dari path
            ImageIcon icon = new ImageIcon(currentQuestion.image_path);

            // opsional: resize biar pas di JLabel
            Image scaled = icon.getImage().getScaledInstance(
                    questionImgLabel.getWidth(),
                    questionImgLabel.getHeight(),
                    Image.SCALE_SMOOTH
            );

            questionImgLabel.setIcon(new ImageIcon(scaled));
            questionImgLabel.setText(""); // jangan ada teks biar gambar terlihat
        }

        setAnswerToLabel(answerAImg, currentQuestion.answers.get(0).image_answer,currentQuestion.answers.get(0).answer);
        setAnswerToLabel(answerBImg, currentQuestion.answers.get(1).image_answer,currentQuestion.answers.get(1).answer);
        setAnswerToLabel(answerCImg, currentQuestion.answers.get(2).image_answer,currentQuestion.answers.get(2).answer);
        setAnswerToLabel(answerDImg, currentQuestion.answers.get(3).image_answer,currentQuestion.answers.get(3).answer);

         List<String> previous = answer_repo.getSelectedAnswers(siswaId, questionId);

        checkBoxA.setSelected(previous.contains("A"));
        checkBoxB.setSelected(previous.contains("B"));
        checkBoxC.setSelected(previous.contains("C"));
        checkBoxD.setSelected(previous.contains("D"));

        // set behavior tergantung tipe soal
        if ("SINGLE_CHOICES".equalsIgnoreCase(currentQuestion.answer_type)) {
            setupSingleChoiceMode();
        } else if ("MULTIPLE_CHOICES".equalsIgnoreCase(currentQuestion.answer_type)) {
            setupMultipleChoiceMode();
        }
        
      
    }
    }
    
    private void setAnswerToLabel(JLabel label, String path, String answer) {
    if (path != null && !path.equals("")) {
        ImageIcon icon = new ImageIcon(path);
        Image scaled = icon.getImage().getScaledInstance(
                label.getWidth(),
                label.getHeight(),
                Image.SCALE_SMOOTH
        );
        label.setIcon(new ImageIcon(scaled));
        label.setText(null);
    } else {
        label.setIcon(null);
        label.setText("<html><body style='width: 200px'>" + answer +"</body></html>");
    }
}

    
        private void setupSingleChoiceMode() {
        // tambahkan listener supaya hanya 1 boleh dicentang
        javax.swing.JCheckBox[] boxes = {checkBoxA, checkBoxB, checkBoxC, checkBoxD};
        for (JCheckBox box : boxes) {
            for (java.awt.event.ActionListener al : box.getActionListeners()) {
                box.removeActionListener(al); // reset listener biar ga dobel
            }
            box.addActionListener(e -> {
                if (box.isSelected()) {
                    // unselect semua selain yg ini
                    for (JCheckBox other : boxes) {
                        if (other != box) {
                            other.setSelected(false);
                        }
                    }
                }
            });
        }
    }

    private void setupMultipleChoiceMode() {
        // untuk multiple choice tidak perlu batasi,
        // tapi pastikan listener tidak bikin auto-unselect
        javax.swing.JCheckBox[] boxes = {checkBoxA, checkBoxB, checkBoxC, checkBoxD};
        for (JCheckBox box : boxes) {
            for (java.awt.event.ActionListener al : box.getActionListeners()) {
                box.removeActionListener(al);
            }
            // listener kosong aja (boleh dicentang bebas)
            box.addActionListener(e -> {});
        }
    }

    
    private void nextQuestion() {
         if (currentIndex < randomizedIds.size() - 1) {
            currentIndex++;
            loadQuestion();
        } else {
             this.globalTimer.stop();
            new form.DoneForm().setVisible(true);
            this.dispose();
        }
    }

    private void prevQuestion() {
         if (currentIndex > 0) {
            currentIndex--;
            loadQuestion();
        }
    }
    
    private void saveCurrentAnswer() {
    if (currentQuestion == null) return;

    int siswaId = Session.getCurrentSiswaId();

    // Hapus dulu jawaban lama untuk soal ini biar bersih
    answer_repo.deleteAnswerByQuestion(currentQuestion.id, siswaId);

    
    if ("SINGLE_CHOICES".equalsIgnoreCase(currentQuestion.answer_type)) {
        // Single choice: hanya 1 jawaban
        Integer selectedAnswerId = null;
        
        if (checkBoxA.isSelected()) selectedAnswerId = currentQuestion.answers.get(0).id;
        if (checkBoxB.isSelected()) selectedAnswerId = currentQuestion.answers.get(1).id;
        if (checkBoxC.isSelected()) selectedAnswerId = currentQuestion.answers.get(2).id;
        if (checkBoxD.isSelected()) selectedAnswerId = currentQuestion.answers.get(3).id;
        if (selectedAnswerId != null) {
            
            answer_repo.upsertAnswer(selectedAnswerId, siswaId);
        }
    } 
    else if ("MULTIPLE_CHOICES".equalsIgnoreCase(currentQuestion.answer_type)) {
        // Multiple choice: bisa banyak jawaban, loop semua
        if (checkBoxA.isSelected()) answer_repo.upsertAnswer(currentQuestion.answers.get(0).id, siswaId);
        if (checkBoxB.isSelected()) answer_repo.upsertAnswer(currentQuestion.answers.get(1).id, siswaId);
        if (checkBoxC.isSelected()) answer_repo.upsertAnswer(currentQuestion.answers.get(2).id, siswaId);
        if (checkBoxD.isSelected()) answer_repo.upsertAnswer(currentQuestion.answers.get(3).id, siswaId);
    }
}

    
    private void startGlobalTimer(int totalSeconds) {
    // stop timer lama kalau ada
    if (globalTimer != null && globalTimer.isRunning()) {
        globalTimer.stop();
    }

    totalTimeLeft = totalSeconds;
    questionProgress.setMaximum(totalSeconds);
    questionProgress.setValue(totalSeconds);

    globalTimer = new Timer(1000, e -> {
        totalTimeLeft--;
        questionProgress.setValue(totalTimeLeft);

        // opsional: update label biar ada countdown mm:ss
        int minutes = totalTimeLeft / 60;
        int seconds = totalTimeLeft % 60;
        timeLabel.setText(String.format("%02d:%02d", minutes, seconds));

        if (totalTimeLeft <= 0) {
            saveCurrentAnswer();
            globalTimer.stop();
            this.dispose(); // selesai ujian
            new form.DoneForm().setVisible(true);
        }
    });

    globalTimer.start();
    }
    
  
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel8 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        answerBImg = new javax.swing.JLabel();
        checkBoxC = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        questionProgress = new javax.swing.JProgressBar();
        timeLabel = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        questionImgLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        questionTextArea = new javax.swing.JTextArea();
        noSoal = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        checkBoxA = new javax.swing.JCheckBox();
        answerAImg = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        checkBoxB = new javax.swing.JCheckBox();
        answerCImg = new javax.swing.JLabel();
        levelLabel = new javax.swing.JLabel();
        answerDImg = new javax.swing.JLabel();
        checkBoxD = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        jPanel8.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        answerBImg.setText("jLabel4");

        checkBoxC.setText("B");
        checkBoxC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBoxCActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(checkBoxC, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(answerBImg, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 196, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(checkBoxC, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(answerBImg, javax.swing.GroupLayout.DEFAULT_SIZE, 196, Short.MAX_VALUE)
                .addContainerGap())
        );

        timeLabel.setText("jLabel1");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(115, 115, 115)
                .addComponent(timeLabel)
                .addContainerGap(108, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(questionProgress, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(37, Short.MAX_VALUE)
                .addComponent(timeLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(questionProgress, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35))
        );

        questionImgLabel.setText("Gambar");

        questionTextArea.setEditable(false);
        questionTextArea.setColumns(20);
        questionTextArea.setLineWrap(true);
        questionTextArea.setRows(5);
        questionTextArea.setWrapStyleWord(true);
        questionTextArea.setFocusable(false);
        questionTextArea.setOpaque(false);
        jScrollPane1.setViewportView(questionTextArea);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(questionImgLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 657, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(8, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(questionImgLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE))
                .addContainerGap())
        );

        noSoal.setText("jLabel1");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(noSoal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(noSoal)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jButton2.setText("Previous");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 192, Short.MAX_VALUE)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jButton1.setText("Next");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        checkBoxA.setText("A");
        checkBoxA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBoxAActionPerformed(evt);
            }
        });

        answerAImg.setText("jLabel1");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(checkBoxA, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(answerAImg, javax.swing.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE))
                .addGap(0, 3, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(checkBoxA, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(answerAImg, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        checkBoxB.setText("C");
        checkBoxB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBoxBActionPerformed(evt);
            }
        });

        answerCImg.setText("jLabel3");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(checkBoxB, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(answerCImg, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(checkBoxB, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(answerCImg, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        levelLabel.setText("jLabel1");

        answerDImg.setText("jLabel2");

        checkBoxD.setText("D");
        checkBoxD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBoxDActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGap(117, 117, 117)
                                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                                .addGap(120, 120, 120)
                                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(231, 231, 231)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(checkBoxD, javax.swing.GroupLayout.DEFAULT_SIZE, 192, Short.MAX_VALUE)
                                    .addComponent(answerDImg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(levelLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(233, 233, 233))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(levelLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(31, 31, 31)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(checkBoxD, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(answerDImg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(404, 404, 404)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(236, Short.MAX_VALUE))
        );

        jScrollPane2.setViewportView(jPanel8);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(15, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1359, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 975, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 6, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void checkBoxBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBoxBActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_checkBoxBActionPerformed

    private void checkBoxDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBoxDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_checkBoxDActionPerformed

    private void checkBoxAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBoxAActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_checkBoxAActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        saveCurrentAnswer();
        nextQuestion();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
       prevQuestion();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void checkBoxCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBoxCActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_checkBoxCActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel answerAImg;
    private javax.swing.JLabel answerBImg;
    private javax.swing.JLabel answerCImg;
    private javax.swing.JLabel answerDImg;
    private javax.swing.JCheckBox checkBoxA;
    private javax.swing.JCheckBox checkBoxB;
    private javax.swing.JCheckBox checkBoxC;
    private javax.swing.JCheckBox checkBoxD;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel levelLabel;
    private javax.swing.JLabel noSoal;
    private javax.swing.JLabel questionImgLabel;
    private javax.swing.JProgressBar questionProgress;
    private javax.swing.JTextArea questionTextArea;
    private javax.swing.JLabel timeLabel;
    // End of variables declaration//GEN-END:variables
}
