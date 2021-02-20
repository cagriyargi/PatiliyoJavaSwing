Hayvanlar.java Dosyasının İçindekiler
package com.mycompany.patiliyo;
public class Hayvanlar 
{
private String turu;
private String adresi;
private int sayisi;
private byte[] picture;
public Hayvanlar(String Turu, String Adresi, int Sayisi,byte[] himg)
{
this.turu = Turu;
this.adresi = Adresi;
this.sayisi = Sayisi;
this.picture = himg;
    	}
  public String getTuru(){return turu ;}
  public String getAdresi(){return adresi ;}
  public int getSayisi(){return sayisi ;}
  public byte[] getImage (){return picture}
}
Şehir Renk Kodları
//sehir renk rgb belirlenmesi sondaki 0 saydamlık için gerekli ekstra 
//1
Color afsin = new Color(255,0,0,0);
Color elbistan = new Color(255,0,255,0);
Color goksun = new Color(0,255,0,0);
Color ekinozu = new Color(0,255,255,0);
Color nurhak = new Color(255,255,0,0);
Color merkez = new Color(255,170,0,0);
Color caglayan = new Color(75,75,75,0);
Color andirin = new Color(0,6,255,0);
Color turkoglu = new Color(0,150,0,0);
Color pazarcik = new Color(125,75,0,0);
Color co = null;
Database Bağlantısı Kodları
public Connection getConnection()
   {Connection con=null; 
        try {
con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/database1?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC","root","mysql");
return con;
        	} catch (SQLException ex) {
Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);
JOptionPane.showMessageDialog(null,"BAĞLANTI BAŞARISIZ");
return null;
}
   }
Input Kontrolü Kodları
public boolean checkInputs(){
if(txt_Tur.getText() == null || txt_Adres.getText() == null || txt_Sayisi.getText() == null) {return false;}else {
           try{
Integer.parseInt(txt_Sayisi.getText());
return true;
}
catch (Exception ex){
return false;}}}
Resim Boyutlandırma Kodları
public ImageIcon ResizeImage(String imagePath, byte[] pic){
ImageIcon myImage = null;
if(imagePath != null)
       {myImage = new ImageIcon(imagePath);
        }else{myImage = new ImageIcon(pic);}
Image img = myImage.getImage();
Image img2 = img.getScaledInstance(lbl_foto.getWidth(), lbl_foto.getHeight(), Image.SCALE_SMOOTH);
        ImageIcon image = new ImageIcon(img2);
        return image; }
Verileri Dizi İçine Alma Kodları
public ArrayList<Hayvanlar> getHayvanList() {
ArrayList<Hayvanlar> hayvanList = new ArrayList<Hayvanlar>();
Connection con = getConnection();
String query = "SELECT * FROM hayvanlar";
if(hayvanAdresi!= null) query+=" WHERE adresihayvanlar=\""+hayvanAdresi+"\"";
Statement st;
ResultSet rs;
        try
        {
st = con.createStatement();
rs = st.executeQuery(query);
Hayvanlar hayvanlar;          
    	while(rs.next())
            {
hayvanlar = new Hayvanlar(rs.getString("turuhayvanlar"), rs.getString("adresihayvanlar"), rs.getInt("sayisihayvanlar"), rs.getBytes("fotohayvanlar"));
                hayvanList.add(hayvanlar);
            } 
        }
        catch (SQLException ex)
        {
            Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        return hayvanList;}
Tabloyu Doldurma Kodları
public void Show_Hayvanlar_In_Table()
    {
        ArrayList<Hayvanlar> list = getHayvanList();
        DefaultTableModel model = (DefaultTableModel)jTable_Hayvanlar.getModel();
        //tablo temizlemek için
        model.setRowCount(0);
        Object[] row= new Object[3];
        for(int i=0; i<list.size();i++)
        {
            row[0] = list.get(i).getTuru();
            row[1] = list.get(i).getAdresi();
            row[2] = list.get(i).getSayisi();
            model.addRow(row);
        }  }
public void ShowItem(int index)
    {
        txt_Tur.setText(getHayvanList().get(index).getTuru());
        txt_Adres.setText(getHayvanList().get(index).getAdresi());
        txt_Sayisi.setText(Integer.toString(getHayvanList().get(index).getSayisi()));
        lbl_foto.setIcon(ResizeImage(null, getHayvanList().get(index).getImage()));
    }
Farenin Konumunu Belirleme Kodları
private int[] pointerKonum()
    {
int x = 0;
int y = 0;
        
        try
        {
Robot r = new Robot();
Rectangle screenRect = getBounds();
r.createScreenCapture(screenRect);
PointerInfo a = MouseInfo.getPointerInfo();
Point b = a.getLocation();
x = (int) b.getX();
y = (int) b.getY();
        }
        catch(AWTException ex){}
int dizi[] = {x, y};
return dizi;
    }
Foto Seç Tuşunun Kodları
private void jB1_ImgChoActionPerformed(java.awt.event.ActionEvent evt) {                                           
JFileChooser file = new JFileChooser();
file.setCurrentDirectory(new File(System.getProperty("user.home")));
FileNameExtensionFilter filter = new FileNameExtensionFilter("*.images", "jpg","png");
file.addChoosableFileFilter(filter);
int result = file.showSaveDialog(null);
if(result == JFileChooser.APPROVE_OPTION)
        {
File selectedFile = file.getSelectedFile();
String path = selectedFile.getAbsolutePath();
lbl_foto.setIcon(ResizeImage(path,null));
ImgPath = path;
        }
        else
{
System.out.println("DOSYA SEÇİLMEDİ");
}
    }

Ekle Tuşunun Kodları
private void Btn_InsertActionPerformed(java.awt.event.ActionEvent evt) {                                           
        if(checkInputs() && ImgPath != null)
        {
            try {
                Connection con = getConnection();
                PreparedStatement ps= con.prepareStatement("INSERT INTO hayvanlar(turuhayvanlar,adresihayvanlar,sayisihayvanlar,fotohayvanlar)" + "values(?,?,?,?)");
ps.setString(1,txt_Tur.getText());
ps.setString(2,txt_Adres.getText());
ps.setString(3,txt_Sayisi.getText());
                InputStream img = new FileInputStream(new File(ImgPath));
ps.setBlob(4, img); 
ps.executeUpdate();
Show_Hayvanlar_In_Table();
JOptionPane.showMessageDialog(null, "Veri Eklendi"); 
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        }else{
            JOptionPane.showMessageDialog(null, "Bir veya daha fazla alan boş bırakıldı.");
        }       
    }      
Güncelle Tuşunun Kodları
private void Btn_UpdateActionPerformed(java.awt.event.ActionEvent evt) {                                           
        if(checkInputs() && txt_Tur.getText() != null)
        {
 String UpdateQuery = null;
 PreparedStatement ps = null;
            Connection con = getConnection();
            //fotosuz update
            if(ImgPath == null)
            {
                try {
                    UpdateQuery = "UPDATE hayvanlar SET sayisihayvanlar = ?" + " WHERE (adresihayvanlar = ? AND turuhayvanlar = ?)";
                    
ps = con.prepareStatement(UpdateQuery);
ps.setInt(1,Integer.parseInt(txt_Sayisi.getText()) );
ps.setString(2, txt_Adres.getText());
ps.setString(3, txt_Tur.getText());
ps.executeUpdate();
Show_Hayvanlar_In_Table();
JOptionPane.showMessageDialog(null, "Veri Güncellendi");     
                } catch (SQLException ex) {
                    Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            //fotolu update
            else{
                try{
                InputStream img= new FileInputStream(new File(ImgPath));
                UpdateQuery = "UPDATE hayvanlar SET sayisihayvanlar = ?, fotohayvanlar = ?" + " WHERE (adresihayvanlar = ? AND turuhayvanlar = ?)";
                
ps = con.prepareStatement(UpdateQuery);
ps.setInt(1,Integer.parseInt(txt_Sayisi.getText()) );
ps.setBlob(2, img);
ps.setString(3, txt_Adres.getText());
ps.setString(4, txt_Tur.getText());
ps.executeUpdate();
Show_Hayvanlar_In_Table();
JOptionPane.showMessageDialog(null, "Veri Güncellendi");

                }catch(Exception ex)
                {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }
        }else{
            JOptionPane.showMessageDialog(null, "Bir yada daha fazla alan boş veya yanlış girildi");
        }
    }     
Çıkar Tuşunun Kodları
private void Btn_DeleteActionPerformed(java.awt.event.ActionEvent evt) {
        if(!txt_Adres.getText().equals(""))
        {
            try 
            {
Connection con = getConnection();
PreparedStatement ps;
ps = con.prepareStatement("DELETE FROM hayvanlar WHERE (adresihayvanlar=? AND turuhayvanlar = ?)");
ps.setString(1, txt_Adres.getText());
ps.setString(2, txt_Tur.getText());
ps.executeUpdate();
Show_Hayvanlar_In_Table();
JOptionPane.showMessageDialog(null, "Bilgi Silindi");
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, "Bilgi SİLİNMEDİ");
            }  
        }else
        {
            JOptionPane.showMessageDialog(null, "ADRES ve TUR BİLGİSİ GİRİLMEDİĞİ İÇİN BİLGİ SİLİNMEDİ");   
        }   
    }     
Tabloda Tıklama ile Verileri Getiren Kodlar
private void jTable_HayvanlarMouseClicked(java.awt.event.MouseEvent evt) {                                              
int index = jTable_Hayvanlar.getSelectedRow();
ShowItem(index);
Harita Seçme Tuşunun Kodları
private void jb_mapChoActionPerformed(java.awt.event.ActionEvent evt) {                                          
JFileChooser file = new JFileChooser();
file.setCurrentDirectory(new File(System.getProperty("user.home")));
FileNameExtensionFilter filter = new FileNameExtensionFilter("*.images", "jpg","png");
file.addChoosableFileFilter(filter);
int result = file.showSaveDialog(null);
        if(result == JFileChooser.APPROVE_OPTION)
        {
File selectedFile = file.getSelectedFile();
String path = selectedFile.getAbsolutePath();
lbl_harita.setIcon(ResizeImage2(path,null));
ImgPath = path;
        }
        else
        {System.out.println("DOSYA SEÇİLMEDİ");}   
    }       
Renkleri Karşılaştırma Kodları
private boolean renkKarsi(Color c1, Color c2)
    {
        if(c1.getRed() == c2.getRed())
        {
            if(c1.getGreen() == c2.getGreen())
            {
                if(c1.getBlue() == c2.getBlue())
                {
                    return true;
                }
            }}
        return false;}    
private void lbl_haritaMouseClicked(java.awt.event.MouseEvent evt) {                                        
        int dizi[] = {pointerKonum()[0],pointerKonum()[1]};
        try
        {
            Robot r = new Robot();
            co = r.getPixelColor(dizi[0], dizi[1]);
            if(renkKarsi(co, afsin))
            {
                hayvanAdresi = "afsin";
            } else if(renkKarsi(co, elbistan)) 
            {
               hayvanAdresi = "elbistan";
            } else if(renkKarsi(co, goksun)) 
            {
                hayvanAdresi = "goksun";
            } else if(renkKarsi(co, ekinozu)) 
            {
                hayvanAdresi = "ekinozu";
            } else if(renkKarsi(co, nurhak)) 
            {
                hayvanAdresi = "nurhak";
            } else if(renkKarsi(co, merkez)) 
            {
                hayvanAdresi = "merkez";
            } else if(renkKarsi(co, caglayan)) 
            {
                hayvanAdresi = "caglayan";
            } else if(renkKarsi(co, andirin)) 
            {
                hayvanAdresi = "andirin";
            } else if(renkKarsi(co, turkoglu)) 
            {
                hayvanAdresi = "turkoglu";
            } else if(renkKarsi(co, pazarcik)) 
            {
                hayvanAdresi = "pazarcik";
            }else{
                hayvanAdresi=null;
            }
            jb_mapCho.setText(hayvanAdresi);
            Show_Hayvanlar_In_Table();    
        }
        catch(AWTException ex){}}                              
