/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.tugas.quizmath_player.form;


import com.tugas.quizmath_player.entity.Answer;
import com.tugas.quizmath_player.entity.QuestionManipulation;
import com.tugas.quizmath_player.repository.QuestionRepository;
import com.tugas.quizmath_player.utils.MultiLineCellRenderer;
import java.awt.Component;
import java.awt.Image;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author syafiq
 */
public class SoalForm extends javax.swing.JFrame {
    private QuestionRepository question_repo;
    private String questionImage = null;
    private String answerAImg = null;  
    private String answerBImg = null;  
    private String answerCImg = null;  
    private String answerDImg = null;  
    private int selectedRow = -1;
    private int selectedIdQuestion = -1;
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(SoalForm.class.getName());

    /**
     * Creates new form Dashboard
     */
    public SoalForm() {
       initComponents();    
       setLocationRelativeTo(null); // posisi center
       setExtendedState(JFrame.MAXIMIZED_BOTH); // otomatis full screen
        this.question_repo = new QuestionRepository();
        this.imagePreview.setIcon(null);
        imagePreview.setText(""); // kalau ada text default
        ImgAnswerA.setIcon(null);
        ImgAnswerA.setText("");
        
        ImgAnswerB.setIcon(null);
        ImgAnswerB.setText("");
        
        ImgAnswerC.setIcon(null);
        ImgAnswerC.setText("");
        
        ImgAnswerD.setIcon(null);
        ImgAnswerD.setText("");
        getSoal();
        
    }
    
    private void getSoal() {
    // Kolom tabel
    String[] columnNames = {
        "ID", "Question", "Image", "Answer Type", "Level", "Topic", 
        "Correct Answer", "Answer A","Answer A Image", "Answer B", "Answer B Image", 
        "Answer C", "Answer C Image", "Answer D", "Answer D Image"
    };

    DefaultTableModel model = new DefaultTableModel(columnNames, 0);
   
    List<QuestionManipulation> questions = question_repo.getAllQuestion(this);

    for (QuestionManipulation q : questions) {
        StringBuilder correctAnswer = new StringBuilder();
        for (Answer a: q.answers) {
            if (a.correct) {
                correctAnswer.append(a.label).append(", ");
            }
        }

        Object[] row = {
            q.id,
            q.question_text,
            resizeImage(q.question_image, 100, 80),  // resize sebelum masuk tabel
            q.answer_type,
            q.level,
            q.topic,
            correctAnswer.toString(),
            q.answers.get(0).answer,
            resizeImage(q.answers.get(0).image_answer, 100, 80),
            q.answers.get(1).answer,
            resizeImage(q.answers.get(1).image_answer, 100, 80),
            q.answers.get(2).answer,
            resizeImage(q.answers.get(2).image_answer, 100, 80),
            q.answers.get(3).answer,
            resizeImage(q.answers.get(3).image_answer, 100, 80),
        };

        model.addRow(row);
    }

    tabelSoal.setModel(model);
    tabelSoal.setRowHeight(80);

    // Renderer untuk ImageIcon
    TableCellRenderer imageRenderer = new DefaultTableCellRenderer() {
        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

            JLabel lbl = new JLabel();
            lbl.setHorizontalAlignment(JLabel.CENTER);

            if (value instanceof ImageIcon) {
                lbl.setIcon((ImageIcon) value);
            }
            return lbl;
        }
    };

    // set renderer ke kolom gambar
    int[] imageCols = {2, 8, 10, 12, 14};
    for (int col : imageCols) {
        tabelSoal.getColumnModel().getColumn(col).setCellRenderer(imageRenderer);
    }
    tabelSoal.getColumnModel().getColumn(1).setCellRenderer(new MultiLineCellRenderer());
    tabelSoal.getColumnModel().getColumn(3).setCellRenderer(new MultiLineCellRenderer());
    tabelSoal.getColumnModel().getColumn(5).setCellRenderer(new MultiLineCellRenderer());
    tabelSoal.getColumnModel().getColumn(7).setCellRenderer(new MultiLineCellRenderer());
    tabelSoal.getColumnModel().getColumn(9).setCellRenderer(new MultiLineCellRenderer());
    tabelSoal.getColumnModel().getColumn(11).setCellRenderer(new MultiLineCellRenderer());
    tabelSoal.getColumnModel().getColumn(13).setCellRenderer(new MultiLineCellRenderer());


    // Lebar kolom
    TableColumnModel columnModel = tabelSoal.getColumnModel();
    for (int i = 0; i < columnModel.getColumnCount(); i++) {
        columnModel.getColumn(i).setPreferredWidth(150);
    }
}

// Helper resize
private ImageIcon resizeImage(String path, int width, int height) {
    if (path == null || path.isEmpty()) return null;
    ImageIcon icon = new ImageIcon(path);
    Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
    return new ImageIcon(img);
}


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane9 = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        answerAIsCorrect = new javax.swing.JCheckBox();
        jScrollPane4 = new javax.swing.JScrollPane();
        answerAText = new javax.swing.JTextArea();
        btnImgAnswerA = new javax.swing.JButton();
        ImgAnswerA = new javax.swing.JLabel();
        jButton7 = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        answerBIsCorrect = new javax.swing.JCheckBox();
        jScrollPane5 = new javax.swing.JScrollPane();
        answerBText = new javax.swing.JTextArea();
        ImgAnswerB = new javax.swing.JLabel();
        btnImgAnswerB = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        answerCIsCorrect = new javax.swing.JCheckBox();
        jScrollPane6 = new javax.swing.JScrollPane();
        answerCText = new javax.swing.JTextArea();
        ImgAnswerC = new javax.swing.JLabel();
        btnImgAnswerC = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        answerDIsCorrect = new javax.swing.JCheckBox();
        jScrollPane7 = new javax.swing.JScrollPane();
        answerDText = new javax.swing.JTextArea();
        ImgAnswerD = new javax.swing.JLabel();
        btnImgAnswerD = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jDesktopPane1 = new javax.swing.JDesktopPane();
        jDesktopPane2 = new javax.swing.JDesktopPane();
        jPanel10 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        save = new javax.swing.JButton();
        update = new javax.swing.JButton();
        delete = new javax.swing.JButton();
        refresh = new javax.swing.JButton();
        clearBtn = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        topicTextArea = new javax.swing.JTextArea();
        buttonUpload = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        levelComboBox = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        questionTextArea = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        imagePreview = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelSoal = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jScrollPane9.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane9.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        jLabel8.setText("Answer A :");

        answerAIsCorrect.setText("Is Correct");

        answerAText.setColumns(20);
        answerAText.setRows(5);
        jScrollPane4.setViewportView(answerAText);

        btnImgAnswerA.setText("Upload");
        btnImgAnswerA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImgAnswerAActionPerformed(evt);
            }
        });

        ImgAnswerA.setText("jLabel4");

        jButton7.setText("Hapus");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(answerAIsCorrect)
                        .addGap(49, 49, 49)
                        .addComponent(btnImgAnswerA, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel8)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 333, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                .addComponent(ImgAnswerA, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnImgAnswerA)
                            .addComponent(answerAIsCorrect)
                            .addComponent(jButton7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(ImgAnswerA, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jLabel17.setText("Answer B :");

        answerBIsCorrect.setText("Is Correct");

        answerBText.setColumns(20);
        answerBText.setRows(5);
        jScrollPane5.setViewportView(answerBText);

        ImgAnswerB.setText("jLabel9");

        btnImgAnswerB.setText("Upload");
        btnImgAnswerB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImgAnswerBActionPerformed(evt);
            }
        });

        jButton9.setText("Hapus");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 366, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(answerBIsCorrect)
                        .addGap(64, 64, 64)
                        .addComponent(btnImgAnswerB)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(12, 12, 12)
                .addComponent(ImgAnswerB, javax.swing.GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ImgAnswerB, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(answerBIsCorrect)
                            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btnImgAnswerB)
                                .addComponent(jButton9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addContainerGap())
        );

        jLabel19.setText("Answer C :");

        answerCIsCorrect.setText("Is Correct");

        answerCText.setColumns(20);
        answerCText.setRows(5);
        jScrollPane6.setViewportView(answerCText);

        ImgAnswerC.setText("jLabel7");

        btnImgAnswerC.setText("Upload");
        btnImgAnswerC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImgAnswerCActionPerformed(evt);
            }
        });

        jButton8.setText("Hapus");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(answerCIsCorrect)
                        .addGap(56, 56, 56)
                        .addComponent(btnImgAnswerC)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 323, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                .addComponent(ImgAnswerC, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel19)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnImgAnswerC)
                            .addComponent(answerCIsCorrect)
                            .addComponent(jButton8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(ImgAnswerC, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jLabel21.setText("Answer D :");

        answerDIsCorrect.setText("Is Correct");

        answerDText.setColumns(20);
        answerDText.setRows(5);
        jScrollPane7.setViewportView(answerDText);

        ImgAnswerD.setText("jLabel10");

        btnImgAnswerD.setText("Upload");
        btnImgAnswerD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImgAnswerDActionPerformed(evt);
            }
        });

        jButton10.setText("Hapus");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel21)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(answerDIsCorrect)
                        .addGap(55, 55, 55)
                        .addComponent(btnImgAnswerD)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 356, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(ImgAnswerD, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel21)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btnImgAnswerD)
                                .addComponent(jButton10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(answerDIsCorrect)))
                    .addComponent(ImgAnswerD, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(51, 204, 255));

        save.setText("Tambah");
        save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveActionPerformed(evt);
            }
        });

        update.setText("Update");
        update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateActionPerformed(evt);
            }
        });

        delete.setText("Hapus");
        delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteActionPerformed(evt);
            }
        });

        refresh.setText("Refresh");
        refresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshActionPerformed(evt);
            }
        });

        clearBtn.setText("Clear");
        clearBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(update, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(delete, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(save, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(refresh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(clearBtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(save)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(update)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(delete)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(refresh)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(clearBtn)
                .addContainerGap(9, Short.MAX_VALUE))
        );

        topicTextArea.setColumns(20);
        topicTextArea.setRows(5);
        jScrollPane3.setViewportView(topicTextArea);

        buttonUpload.setText("Upload gambar");
        buttonUpload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonUploadActionPerformed(evt);
            }
        });

        jLabel3.setText("Question Image :");

        levelComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "MUDAH", "NORMAL", "SUSAH" }));

        jLabel5.setText("Level :");

        jLabel6.setText("Topic :");

        questionTextArea.setColumns(20);
        questionTextArea.setRows(5);
        jScrollPane2.setViewportView(questionTextArea);

        jLabel2.setText("Question text :");

        imagePreview.setText("jLabel7");

        jButton5.setText("Hapus gambar");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 397, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 318, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel6)
                                            .addComponent(levelComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel5)
                                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                .addComponent(jButton5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(buttonUpload, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE)))
                                        .addGap(83, 83, 83)
                                        .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addGap(36, 36, 36)
                                .addComponent(imagePreview, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(109, 109, 109))))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addComponent(buttonUpload, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel5)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(levelComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel6))
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 12, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(imagePreview, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 1044, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(204, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jDesktopPane2.setLayer(jPanel10, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jDesktopPane2Layout = new javax.swing.GroupLayout(jDesktopPane2);
        jDesktopPane2.setLayout(jDesktopPane2Layout);
        jDesktopPane2Layout.setHorizontalGroup(
            jDesktopPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDesktopPane2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jDesktopPane2Layout.setVerticalGroup(
            jDesktopPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDesktopPane2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jDesktopPane1.setLayer(jDesktopPane2, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jDesktopPane1Layout = new javax.swing.GroupLayout(jDesktopPane1);
        jDesktopPane1.setLayout(jDesktopPane1Layout);
        jDesktopPane1Layout.setHorizontalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDesktopPane1Layout.createSequentialGroup()
                .addComponent(jDesktopPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 175, Short.MAX_VALUE))
        );
        jDesktopPane1Layout.setVerticalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDesktopPane1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jDesktopPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        tabelSoal.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Id", "Question Text", "Question Image", "Answer type", "Level", "Topic", "Question type"
            }
        ));
        tabelSoal.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tabelSoal.setShowGrid(true);
        tabelSoal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                SoalForm.this.mouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabelSoal);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jDesktopPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1662, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(397, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jDesktopPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 323, Short.MAX_VALUE)
                .addContainerGap())
        );

        jScrollPane9.setViewportView(jPanel2);

        jPanel1.setBackground(new java.awt.Color(0, 153, 255));
        jPanel1.setForeground(new java.awt.Color(153, 255, 255));

        jLabel1.setFont(new java.awt.Font("Noto Sans CJK HK Light", 1, 18)); // NOI18N
        jLabel1.setText("Hello admin");

        jButton1.setText("Soal");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Leaderboard");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Daftar Siswa");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton6.setText("Nilai");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton11.setText("Keluar");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(63, 63, 63)
                .addComponent(jLabel1)
                .addContainerGap(66, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 1673, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 934, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private int[] hitungScore() {
    int[] scores = new int[4]; // index 0=A, 1=B, 2=C, 3=D
    int correctCount = 0;

    if (answerAIsCorrect.isSelected()) correctCount++;
    if (answerBIsCorrect.isSelected()) correctCount++;
    if (answerCIsCorrect.isSelected()) correctCount++;
    if (answerDIsCorrect.isSelected()) correctCount++;

    if (correctCount == 0) {
        JOptionPane.showMessageDialog(rootPane, "Harus ada jawaban yang benar !");
        return scores; // semua 0
    }

    int baseScore = 100 / correctCount;
    int remainder = 100 % correctCount;

    if (answerAIsCorrect.isSelected()) {
        scores[0] = baseScore + (remainder > 0 ? 1 : 0);
        if (remainder > 0) remainder--;
    }
    if (answerBIsCorrect.isSelected()) {
        scores[1] = baseScore + (remainder > 0 ? 1 : 0);
        if (remainder > 0) remainder--;
    }
    if (answerCIsCorrect.isSelected()) {
        scores[2] = baseScore + (remainder > 0 ? 1 : 0);
        if (remainder > 0) remainder--;
    }
    if (answerDIsCorrect.isSelected()) {
        scores[3] = baseScore + (remainder > 0 ? 1 : 0);
        if (remainder > 0) remainder--;
    }

    return scores;
}

    
    private boolean validateSoal() {
        if (questionTextArea.getText().equals("")) {
            JOptionPane.showMessageDialog(rootPane, "Soal harus diisi !");
            return false;
        }
        
        if (topicTextArea.getText().equals("")) {
            JOptionPane.showMessageDialog(rootPane, "Topic soal harus diisi !");
            return false;
        }
        
        if (answerAText.getText().equals("") || answerBText.getText().equals("") || answerCText.getText().equals("") || answerDText.getText().equals("")) {
            JOptionPane.showMessageDialog(rootPane, "Masing masing jawaban harus diisi !");
            return false;
        }
        return true;
    }
    
    private String validateAnswerType() {
                String answerType = "";

        int correctCount = 0;
        if (answerAIsCorrect.isSelected()) correctCount++;
        if (answerBIsCorrect.isSelected()) correctCount++;
        if (answerCIsCorrect.isSelected()) correctCount++;
        if (answerDIsCorrect.isSelected()) correctCount++;

        switch (correctCount) {
            case 0 -> JOptionPane.showMessageDialog(rootPane, "Harus ada jawaban yang benar !");
            case 1 -> answerType = "SINGLE_CHOICES";
            case 4 -> JOptionPane.showMessageDialog(rootPane, "Tidak boleh semua jawaban benar !");
            default -> answerType = "MULTIPLE_CHOICES";
        }
        return answerType;
    }
    
    private void saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveActionPerformed
     if (this.selectedRow != -1) {
        int result = JOptionPane.showConfirmDialog(this, """
                                                         Anda telah memilih data yang ada.
                                                         Apakah Anda ingin menambah data baru?
                                                         
                                                         Klik OK untuk menambah data baru.
                                                         Klik Cancel jika ingin mengedit menggunakan tombol Update.""",
            "Konfirmasi",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            // Reset mode ke 'tambah data baru'
            this.selectedRow = -1;
            // Panggil fungsi ini ulang supaya lanjut proses simpan data baru
            saveActionPerformed(evt);
        }
        return;
    }

    // ======= Mode tambah data baru =======
   if(!validateSoal()) return;

    String answerType = validateAnswerType();
    if (answerType.equals("")) {
        return;
    }
    int[] scores = hitungScore();

    List<Answer> answers = new ArrayList<>();
    answers.add(new Answer(answerAText.getText(), "A", scores[0], answerAIsCorrect.isSelected(), this.answerAImg));
    answers.add(new Answer(answerBText.getText(), "B", scores[1], answerBIsCorrect.isSelected(), this.answerBImg));
    answers.add(new Answer(answerCText.getText(), "C", scores[2], answerCIsCorrect.isSelected(), this.answerCImg));
    answers.add(new Answer(answerDText.getText(), "D", scores[3], answerDIsCorrect.isSelected(), this.answerDImg));

    QuestionManipulation question = new QuestionManipulation(
        0,
        questionTextArea.getText(),
        questionImage,
        answerType,
        levelComboBox.getSelectedItem().toString(),
        topicTextArea.getText(),
        answers
    );

    question_repo.createSoal(question, this);
    getSoal();
    clear();
    }//GEN-LAST:event_saveActionPerformed

    private void refreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshActionPerformed
       getSoal();
    }//GEN-LAST:event_refreshActionPerformed

    private void buttonUploadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonUploadActionPerformed
        JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Pilih Gambar");

            // filter file hanya gambar
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                    "jpg", "jpeg", "png", "webp"));

            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                questionImage = selectedFile.getAbsolutePath();

                // set preview gambar ke JLabel
                ImageIcon icon = new ImageIcon(questionImage);
                int width = imagePreview.getWidth();
                int height = imagePreview.getHeight();
                // resize biar pas
                Image img = icon.getImage().getScaledInstance(width,height, Image.SCALE_SMOOTH);
                imagePreview.setIcon(new ImageIcon(img));
                imagePreview.setBorder(BorderFactory.createTitledBorder("Preview Gambar"));
                imagePreview.setText(""); // hapus tulisan default
            }
    }//GEN-LAST:event_buttonUploadActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
       new LeaderboardForm().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        new DaftarSiswaForm().setVisible(true);
         this.dispose();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
       new NilaiForm().setVisible(true);
       this.dispose();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteActionPerformed
       if (this.selectedRow > -1) {
            int confirm = JOptionPane.showConfirmDialog(
                    this, 
                    "Apakah Anda yakin ingin menghapus data ini?", 
                    "Konfirmasi Hapus", 
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                this.question_repo.deleteSoal(this.selectedIdQuestion, this);
            }
       } else {
           JOptionPane.showConfirmDialog(this, "Silahkan pilih soal di tabel bawah yang akan dihapus");
       }
      
       clear();
       getSoal();
    }//GEN-LAST:event_deleteActionPerformed

   

    
    private void mouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mouseClicked
        this.selectedRow = tabelSoal.getSelectedRow();   // ambil baris yang diklik
        System.out.println(this.selectedRow);
        if (this.selectedRow > -1) {
            // ambil data tiap kolom
            this.selectedIdQuestion = Integer.parseInt(tabelSoal.getValueAt(selectedRow, 0).toString());
            String question = tabelSoal.getValueAt(selectedRow, 1).toString();
            Object imagePath = tabelSoal.getValueAt(selectedRow, 2);
            String level  = tabelSoal.getValueAt(selectedRow, 4).toString();
            String topic  = tabelSoal.getValueAt(selectedRow, 5).toString();
            String correctAnswer  = tabelSoal.getValueAt(selectedRow, 6).toString();
            String answerA  = tabelSoal.getValueAt(selectedRow, 7).toString();
            Object imgAnswerA = tabelSoal.getValueAt(selectedRow,8);
            String answerB  = tabelSoal.getValueAt(selectedRow, 9).toString();
            Object imgAnswerB = tabelSoal.getValueAt(selectedRow,10);
            String answerC  = tabelSoal.getValueAt(selectedRow, 11).toString();
            Object imgAnswerC = tabelSoal.getValueAt(selectedRow,12);
            String answerD  = tabelSoal.getValueAt(selectedRow, 13).toString();
            Object imgAnswerD = tabelSoal.getValueAt(selectedRow,14);
            
            // isi ke komponen lain
            questionTextArea.setText(question);
             levelComboBox.setSelectedItem(level);
             topicTextArea.setText(topic);
             answerAText.setText(answerA);
              answerBText.setText(answerB);
               answerCText.setText(answerC);
                answerDText.setText(answerD);
                
                
             String[] correctAnswers = correctAnswer.split(", ");
             answerAIsCorrect.setSelected(false);
            answerBIsCorrect.setSelected(false);
            answerCIsCorrect.setSelected(false);
            answerDIsCorrect.setSelected(false);
            for (String ans : correctAnswers) {
                ans = ans.trim(); // buang spasi kalau ada
                switch (ans) {
                    case "A" -> answerAIsCorrect.setSelected(true);
                    case "B" -> answerBIsCorrect.setSelected(true);
                    case "C" -> answerCIsCorrect.setSelected(true);
                    case "D" -> answerDIsCorrect.setSelected(true);
                }
            }
             
            // kalau ada gambar
            if (imagePath != null && imagePath instanceof ImageIcon) {
                ImageIcon icon = (ImageIcon) imagePath;
                imagePreview.setIcon(new ImageIcon(
                    icon.getImage().getScaledInstance(
                        imagePreview.getWidth(),
                        imagePreview.getHeight(),
                        Image.SCALE_SMOOTH
                    )
                ));
            } else {
                imagePreview.setIcon(null);
            }
            
            if (imgAnswerA != null && imgAnswerA instanceof ImageIcon) {
                ImageIcon icon = (ImageIcon) imgAnswerA;
                ImgAnswerA.setIcon(new ImageIcon(
                    icon.getImage().getScaledInstance(
                        ImgAnswerA.getWidth(),
                        ImgAnswerA.getHeight(),
                        Image.SCALE_SMOOTH
                    )
                ));
            } else {
                ImgAnswerA.setIcon(null);
            }
            
            if (imgAnswerB != null && imgAnswerB instanceof ImageIcon) {
                ImageIcon icon = (ImageIcon) imgAnswerB;
                ImgAnswerB.setIcon(new ImageIcon(
                    icon.getImage().getScaledInstance(
                        ImgAnswerB.getWidth(),
                        ImgAnswerB.getHeight(),
                        Image.SCALE_SMOOTH
                    )
                ));
            } else {
                ImgAnswerB.setIcon(null);
            }
             
            if (imgAnswerC != null && imgAnswerC instanceof ImageIcon) {
                ImageIcon icon = (ImageIcon) imgAnswerC;
                ImgAnswerC.setIcon(new ImageIcon(
                    icon.getImage().getScaledInstance(
                        ImgAnswerC.getWidth(),
                        ImgAnswerC.getHeight(),
                        Image.SCALE_SMOOTH
                    )
                ));
            } else {
                ImgAnswerC.setIcon(null);
            }
            
            if (imgAnswerD != null && imgAnswerD instanceof ImageIcon) {
                ImageIcon icon = (ImageIcon) imgAnswerD;
                ImgAnswerD.setIcon(new ImageIcon(
                    icon.getImage().getScaledInstance(
                        ImgAnswerD.getWidth(),
                        ImgAnswerD.getHeight(),
                        Image.SCALE_SMOOTH
                    )
                ));
            } else {
                ImgAnswerD.setIcon(null);
            }
        }
    }//GEN-LAST:event_mouseClicked

    private void btnImgAnswerDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImgAnswerDActionPerformed
       JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Pilih Gambar");

            // filter file hanya gambar
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                    "jpg", "jpeg", "png", "webp"));

            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                answerDImg = selectedFile.getAbsolutePath();

                // set preview gambar ke JLabel
                ImageIcon icon = new ImageIcon(answerDImg);
                int width = ImgAnswerD.getWidth();
                int height = ImgAnswerD.getHeight();
                // resize biar pas
                Image img = icon.getImage().getScaledInstance(width,height, Image.SCALE_SMOOTH);
                ImgAnswerD.setIcon(new ImageIcon(img));
                ImgAnswerD.setBorder(BorderFactory.createTitledBorder("Preview Gambar"));
                ImgAnswerD.setText(""); // hapus tulisan default
            }      
    }//GEN-LAST:event_btnImgAnswerDActionPerformed

    private void btnImgAnswerAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImgAnswerAActionPerformed
       JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Pilih Gambar");

            // filter file hanya gambar
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                    "jpg", "jpeg", "png", "webp"));

            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                answerAImg = selectedFile.getAbsolutePath();

                // set preview gambar ke JLabel
                ImageIcon icon = new ImageIcon(answerAImg);
                int width = ImgAnswerA.getWidth();
                int height = ImgAnswerA.getHeight();
                // resize biar pas
                Image img = icon.getImage().getScaledInstance(width,height, Image.SCALE_SMOOTH);
                ImgAnswerA.setIcon(new ImageIcon(img));
                ImgAnswerA.setBorder(BorderFactory.createTitledBorder("Preview Gambar"));
                ImgAnswerA.setText(""); // hapus tulisan default
            }      
    }//GEN-LAST:event_btnImgAnswerAActionPerformed

    private void btnImgAnswerCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImgAnswerCActionPerformed
     JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Pilih Gambar");

            // filter file hanya gambar
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                    "jpg", "jpeg", "png", "webp"));

            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                answerCImg = selectedFile.getAbsolutePath();

                // set preview gambar ke JLabel
                ImageIcon icon = new ImageIcon(answerCImg);
                int width = ImgAnswerC.getWidth();
                int height = ImgAnswerC.getHeight();
                // resize biar pas
                Image img = icon.getImage().getScaledInstance(width,height, Image.SCALE_SMOOTH);
                ImgAnswerC.setIcon(new ImageIcon(img));
                ImgAnswerC.setBorder(BorderFactory.createTitledBorder("Preview Gambar"));
                ImgAnswerC.setText(""); // hapus tulisan default
            }      
    }//GEN-LAST:event_btnImgAnswerCActionPerformed

    private void btnImgAnswerBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImgAnswerBActionPerformed
    JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Pilih Gambar");

            // filter file hanya gambar
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                    "jpg", "jpeg", "png", "webp"));

            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                answerBImg = selectedFile.getAbsolutePath();

                // set preview gambar ke JLabel
                ImageIcon icon = new ImageIcon(answerBImg);
                int width = ImgAnswerB.getWidth();
                int height = ImgAnswerB.getHeight();
                // resize biar pas
                Image img = icon.getImage().getScaledInstance(width,height, Image.SCALE_SMOOTH);
                ImgAnswerB.setIcon(new ImageIcon(img));
                ImgAnswerB.setBorder(BorderFactory.createTitledBorder("Preview Gambar"));
                ImgAnswerB.setText(""); // hapus tulisan default
            }      
    }//GEN-LAST:event_btnImgAnswerBActionPerformed

    private void clear() {
          this.questionTextArea.setText("");
        this.levelComboBox.setSelectedItem("EASY");
        this.topicTextArea.setText("");
        this.imagePreview.setIcon(null);
        imagePreview.setText(""); // kalau ada text default
        
        answerAText.setText("");
        answerBText.setText("");
        answerCText.setText("");
        answerDText.setText("");
        
        ImgAnswerA.setIcon(null);
        ImgAnswerA.setText("");
        ImgAnswerB.setIcon(null);
        ImgAnswerB.setText("");
        ImgAnswerC.setIcon(null);
        ImgAnswerC.setText("");
        ImgAnswerD.setIcon(null);
        ImgAnswerD.setText("");

        answerAIsCorrect.setSelected(false);
        answerBIsCorrect.setSelected(false);
        answerCIsCorrect.setSelected(false);
        answerDIsCorrect.setSelected(false);
        this.selectedIdQuestion = -1;
        this.selectedRow = -1;
         this.selectedRow = -1;
    }
    private void clearBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearBtnActionPerformed
      
                clear();
    }//GEN-LAST:event_clearBtnActionPerformed

    private void updateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateActionPerformed
      if (selectedRow == -1) {
          JOptionPane.showMessageDialog(
            this,
            "Silakan pilih data terlebih dahulu sebelum melakukan update.",
            "Peringatan",
            JOptionPane.WARNING_MESSAGE
        );
        return;
      }
     // Konfirmasi sebelum update
    int confirm = JOptionPane.showConfirmDialog(
        this,
        "Apakah Anda yakin ingin memperbarui data soal ini?",
        "Konfirmasi Update",
        JOptionPane.OK_CANCEL_OPTION,
        JOptionPane.QUESTION_MESSAGE
    );

    if (confirm != JOptionPane.OK_OPTION) {
        return; // Batalkan update jika user tidak klik OK
    }
    
    if(!validateSoal()) return;

    String answerType = validateAnswerType();
     if (answerType.equals("")) {
        return;
    }
    int[] scores = hitungScore();

    List<Answer> answers = new ArrayList<>();
    answers.add(new Answer(answerAText.getText(), "A", scores[0], answerAIsCorrect.isSelected(), this.answerAImg));
    answers.add(new Answer(answerBText.getText(), "B", scores[1], answerBIsCorrect.isSelected(), this.answerBImg));
    answers.add(new Answer(answerCText.getText(), "C", scores[2], answerCIsCorrect.isSelected(), this.answerCImg));
    answers.add(new Answer(answerDText.getText(), "D", scores[3], answerDIsCorrect.isSelected(), this.answerDImg));

    QuestionManipulation question = new QuestionManipulation(
        this.selectedIdQuestion,
        questionTextArea.getText(),
        questionImage,
        answerType,
        levelComboBox.getSelectedItem().toString(),
        topicTextArea.getText(),
        answers
    );

    question_repo.updateSoal(question, this);
    getSoal();
    clear();
    
    }//GEN-LAST:event_updateActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
      imagePreview.setIcon(null);
      this.questionImage = null;
      imagePreview.setBorder(null);

    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
      ImgAnswerA.setIcon(null);
      this.answerAImg = null;
      ImgAnswerA.setBorder(null);
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
       ImgAnswerC.setIcon(null);
        this.answerCImg = null;
              ImgAnswerC.setBorder(null);

    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        ImgAnswerB.setIcon(null);
         this.answerBImg = null;
               ImgAnswerB.setBorder(null);

    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
       ImgAnswerD.setIcon(null);
        this.answerDImg = null;
              ImgAnswerD.setBorder(null);

    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
     new LoginForm().setVisible(true);
     this.dispose();
    }//GEN-LAST:event_jButton11ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel ImgAnswerA;
    private javax.swing.JLabel ImgAnswerB;
    private javax.swing.JLabel ImgAnswerC;
    private javax.swing.JLabel ImgAnswerD;
    private javax.swing.JCheckBox answerAIsCorrect;
    private javax.swing.JTextArea answerAText;
    private javax.swing.JCheckBox answerBIsCorrect;
    private javax.swing.JTextArea answerBText;
    private javax.swing.JCheckBox answerCIsCorrect;
    private javax.swing.JTextArea answerCText;
    private javax.swing.JCheckBox answerDIsCorrect;
    private javax.swing.JTextArea answerDText;
    private javax.swing.JButton btnImgAnswerA;
    private javax.swing.JButton btnImgAnswerB;
    private javax.swing.JButton btnImgAnswerC;
    private javax.swing.JButton btnImgAnswerD;
    private javax.swing.JButton buttonUpload;
    private javax.swing.JButton clearBtn;
    private javax.swing.JButton delete;
    private javax.swing.JLabel imagePreview;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JDesktopPane jDesktopPane2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JComboBox<String> levelComboBox;
    private javax.swing.JTextArea questionTextArea;
    private javax.swing.JButton refresh;
    private javax.swing.JButton save;
    private javax.swing.JTable tabelSoal;
    private javax.swing.JTextArea topicTextArea;
    private javax.swing.JButton update;
    // End of variables declaration//GEN-END:variables
}
