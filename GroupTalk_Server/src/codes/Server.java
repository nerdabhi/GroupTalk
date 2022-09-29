package codes;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.table.DefaultTableModel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Abhishek Rai
 */

public class Server extends javax.swing.JFrame {
    ServerSocket ss;
    HashMap clientColl=new HashMap();
    public Server(String admin) {
        initComponents();
        setLocationRelativeTo(null);
        setTitle("Welcome "+admin);
        sStatus.setText("SERVER STATUS: Running");
        try {
            ss=new ServerSocket(2089);
            new ClientAccept().start();   
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private void chatListUpdate(){
        DefaultListModel dlm=new DefaultListModel();
        userlist.setModel(dlm);
        dlm.clear();
        String S="";
        Set k=clientColl.keySet();
        Iterator itr=k.iterator();
        while(itr.hasNext()){
            String key=(String)itr.next();
            S+=key+",";
        }
        if(S.length()!=0)
            S=S.substring(0,S.length()-1);
            itr=k.iterator();
        while(itr.hasNext()){
            String key=(String)itr.next(); 
            dlm.addElement(key);
        }
    }
    
    class ClientAccept extends Thread{
        //Connecting the user to the server and start reading the message...
        public void run(){
            while(true){
                try {
                    Socket s=ss.accept();
                    String i=new DataInputStream(s.getInputStream()).readUTF();
                    if(clientColl.containsKey(i)){
                        DataOutputStream dout=new DataOutputStream(s.getOutputStream());
                        dout.writeUTF("Duplicate registration !");
                    }else{
                        clientColl.put(i,s);
                        msgBox.append("Welcome "+i+"\n");
                        DataOutputStream dout=new DataOutputStream(s.getOutputStream());
                        dout.writeUTF("");
                        new MsgRead(s,i).start();
                        new PrepareClientList().start();
                    }          
                } catch (IOException ex) {
                    ex.printStackTrace();
                }                                                    
            }
        }
    }
    
    class MsgRead extends Thread{
        Socket s;
        String ID;
        MsgRead(Socket s,String ID){
            this.s=s;
            this.ID=ID;
        }
        public void run(){   
            while(!clientColl.isEmpty()){
                try {
                    String i=new DataInputStream(s.getInputStream()).readUTF();
                    //Leaving chat group.(Form Closing)
                    if(i.equals("leexaivte")){
                        clientColl.remove(ID);
                        msgBox.append(ID+" left.\n");
                        new PrepareClientList().start();
                        Set k=clientColl.keySet();
                        Iterator itr=k.iterator();
                        while(itr.hasNext()){
                            String key=(String)itr.next();
                            if(!key.equalsIgnoreCase(ID)){
                                try{
                                    new DataOutputStream(((Socket)clientColl.get(key)).getOutputStream()).writeUTF(ID+" left.");
                                }catch (Exception ex) {
                                    clientColl.remove(key);
                                    msgBox.append(key+" left.");
                                    new PrepareClientList().start();
                                }
                            }
                        }
                    }
                    else{
                        //Unicasting the message.
                        if(i.contains("burnoiadcast")){
                            i=i.substring(12);
                            StringTokenizer st=new StringTokenizer(i,":");
                            String id=st.nextToken();
                            i=st.nextToken();
                            try{
                                new DataOutputStream(((Socket)clientColl.get(id)).getOutputStream()).writeUTF(ID+" > "+i);
                            }catch(Exception ex){
                                clientColl.remove(id);
                                msgBox.append("Bye "+id);
                                new PrepareClientList().start();
                            }  
                        }//Broadcasting the Message.
                        else{
                            Set k=clientColl.keySet();
                            Iterator itr=k.iterator();
                            while(itr.hasNext()){
                                String key=(String)itr.next();
                                if(!key.equalsIgnoreCase(ID)){
                                    try{
                                    new DataOutputStream(((Socket)clientColl.get(key)).getOutputStream()).writeUTF(ID+" (Broadcast) > "+i);
                                    } catch (Exception ex) {
                                        clientColl.remove(key);
                                        msgBox.append(key+" left.");
                                        new PrepareClientList().start();
                                    }
                                }
                            }
                        }
                    }
                }catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
    
    class PrepareClientList extends Thread{
        public void run(){
            chatListUpdate();
            try {
            String ids="";
                Set k=clientColl.keySet();
                Iterator itr=k.iterator();
                while(itr.hasNext()){
                    String key=(String)itr.next();
                   ids+=key+",";
                }
                if(ids.length()!=0)
                    ids=ids.substring(0, ids.length()-1);
                itr=k.iterator();
                while(itr.hasNext()){
                    String key=(String)itr.next(); 
                    try{
                        new DataOutputStream(((Socket)clientColl.get(key)).getOutputStream()).writeUTF(":;.,/="+ids);
                    }catch (Exception ex) {
                        clientColl.remove(key);
                        msgBox.append(key+" left.");
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Top = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        sStatus = new javax.swing.JLabel();
        Down = new javax.swing.JPanel();
        Right = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        tbox = new javax.swing.JTextField();
        jPanel14 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        userlist = new javax.swing.JList<>();
        Left = new javax.swing.JPanel();
        profile = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        msgBox = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        Top.setBackground(new java.awt.Color(0, 104, 139));
        Top.setForeground(new java.awt.Color(16, 78, 139));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/iChat.png"))); // NOI18N
        jLabel2.setText("GroupTalk");
        jLabel2.setIconTextGap(6);

        sStatus.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        sStatus.setForeground(new java.awt.Color(255, 255, 255));
        sStatus.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        sStatus.setText("SERVER STATUS: ");

        javax.swing.GroupLayout TopLayout = new javax.swing.GroupLayout(Top);
        Top.setLayout(TopLayout);
        TopLayout.setHorizontalGroup(
            TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TopLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 356, Short.MAX_VALUE)
                .addComponent(sStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        TopLayout.setVerticalGroup(
            TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TopLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(sStatus))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(Top, java.awt.BorderLayout.NORTH);

        Down.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        Right.setLayout(new java.awt.BorderLayout());

        tbox.setForeground(new java.awt.Color(153, 153, 153));
        tbox.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tbox.setText("Search User");
        tbox.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tboxMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                tboxMouseExited(evt);
            }
        });
        tbox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tboxKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tbox, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(tbox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        Right.add(jPanel13, java.awt.BorderLayout.NORTH);

        jPanel14.setLayout(new java.awt.BorderLayout());

        userlist.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 240, 240), 20), "Active users", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12), new java.awt.Color(0, 102, 204))); // NOI18N
        jScrollPane1.setViewportView(userlist);

        jPanel14.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        Right.add(jPanel14, java.awt.BorderLayout.CENTER);

        Down.add(Right, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 0, 250, 480));

        Left.setLayout(new java.awt.BorderLayout());

        profile.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 240, 240), 20), "Session log", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12), new java.awt.Color(0, 102, 204))); // NOI18N

        msgBox.setColumns(20);
        msgBox.setRows(5);
        jScrollPane2.setViewportView(msgBox);

        javax.swing.GroupLayout profileLayout = new javax.swing.GroupLayout(profile);
        profile.setLayout(profileLayout);
        profileLayout.setHorizontalGroup(
            profileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(profileLayout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 460, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        profileLayout.setVerticalGroup(
            profileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, profileLayout.createSequentialGroup()
                .addGap(0, 19, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 413, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        Left.add(profile, java.awt.BorderLayout.CENTER);

        Down.add(Left, new org.netbeans.lib.awtextra.AbsoluteConstraints(253, 0, 500, 480));
        Left.getAccessibleContext().setAccessibleDescription("");

        getContentPane().add(Down, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tboxMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tboxMouseClicked
        String s=tbox.getText();
        if(s.equals("Search User")){
            tbox.setText(null);
        }
    }//GEN-LAST:event_tboxMouseClicked

    private void tboxMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tboxMouseExited
        String s=tbox.getText();
        if(s.equals("")){
            tbox.setText("Search User");
        }
    }//GEN-LAST:event_tboxMouseExited

    private void tboxKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tboxKeyReleased
        String searchSt=(String)tbox.getText();
        
        DefaultListModel dlm=new DefaultListModel();
        userlist.setModel(dlm);
        dlm.clear();
        String S="";
        Set k=clientColl.keySet();
        Iterator itr=k.iterator();
        while(itr.hasNext()){
            String key=(String)itr.next();
            S+=key+",";
        }
        if(S.length()!=0)
            S=S.substring(0,S.length()-1);
        itr=k.iterator();
        while(itr.hasNext()){
            String key=(String)itr.next(); 
            if(key.startsWith(searchSt))
                dlm.addElement(key);
        }
    }//GEN-LAST:event_tboxKeyReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Down;
    private javax.swing.JPanel Left;
    private javax.swing.JPanel Right;
    private javax.swing.JPanel Top;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea msgBox;
    private javax.swing.JPanel profile;
    private javax.swing.JLabel sStatus;
    private javax.swing.JTextField tbox;
    private javax.swing.JList<String> userlist;
    // End of variables declaration//GEN-END:variables
}
