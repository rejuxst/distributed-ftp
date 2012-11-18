����   2]  client  java/lang/Object lwd Ljava/lang/String; rwd username password servers [Lmessaging; server_list [Ljava/lang/String; port_num I qur_size main ([Ljava/lang/String;)V Code
     <init> (II)V  java/io/File  .
     (Ljava/lang/String;)V
    ! " getAbsolutePath ()Ljava/lang/String;	  $   & /	  (  	 * , + java/lang/System - . out Ljava/io/PrintStream; 0 quarac> 
 2 4 3 java/io/PrintStream 5  print 7 java/io/BufferedReader 9 java/io/InputStreamReader	 * ; < = in Ljava/io/InputStream;
 8 ?  @ (Ljava/io/InputStream;)V
 6 B  C (Ljava/io/Reader;)V
 6 E F " readLine H "IO error trying to read your name!
 2 J K  println
 * M N O exit (I)V Q java/util/StringTokenizer
 P 
 P T U V hasMoreElements ()Z
 P X Y Z nextElement ()Ljava/lang/Object; \ java/lang/String
 P ^ _ ` countTokens ()I b quit
 [ d e f equals (Ljava/lang/Object;)Z N i login
  k i  m get
  o m  q put
  s q  u help
  w u  y lls
  { y  } lpwd
   }  � lcd
  � �  � cd
  � �  � ls
  � �  � pwd
  � �  � Invalid Command � java/io/IOException LineNumberTable LocalVariableTable args cl Lclient; br Ljava/io/BufferedReader; ccmd ioe Ljava/io/IOException; Tok Ljava/util/StringTokenizer; cmdstr cmdargc cmdargs i StackMapTable 
  �  � ()V	  �  	  �   � 	messaging	  � 
  � 	127.0.0.1
 � �  � (Ljava/lang/String;I)V this qs pn ip_addr � 
Username: 	  �   � 
Password: 	  � 	  � &IO error trying to read your password! � java/lang/StringBuilder � login as:  
 � 
 � � � � append -(Ljava/lang/String;)Ljava/lang/StringBuilder; �  with: 
 � � � " toString
 � � � � clientLogin '(Ljava/lang/String;)Ljava/lang/Boolean;
 � � � java/lang/Boolean � V booleanValue � Login failed! argv username_hash
 [ � � � valueOf &(Ljava/lang/Object;)Ljava/lang/String;
 � � � � (C)Ljava/lang/StringBuilder;
 � � � � (I)Ljava/lang/StringBuilder; � _
 � � m � '(Ljava/lang/String;Ljava/lang/String;)I 
local_path � 	tempstamp
 � � q � 9(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)I � &Failed to communicate with the server! opcode � CThis program allows for read and write to a remote file repository. � List of Commands:  � help: prints this help message. � Jlogin [username]@[domain]: reloads the login prompt for username and will   ;                           attempt to connect to the domain #quit: Exits this prompt with code 0 Local System Commands: >lls -alF [path]: Displays the local directory path relative to /                 the current working directory.
 Qlcd [path]: Changes the local current working directory to path relative to lpwd. Clpwd: Displays the present working directory on the local computer. Remote System Commands: Mls -alF [path]: Displays the directory path  on the remote system relative to .                the current working directory. Ncd [path]: Changes the current working directory on the remote system to path. Apwd: Displays the present working directory on the remote system. File System Interaction:  >mkdir [name]: creates the directory name on the remote system. Yrmdir [name]: Removes the directory name on the remote system fails if name is not empty. Xrm -rf [name]: Removes the file name. If name is a directory -r will delete recursively.  7put -r [name]: Puts the file name on the remote system." =               If name is a directory -r will add recursively$ 9get -r [name]: Gets the file name from the remote system.& U               If name is a directory -r will recursively get the directory structure( (This does not accept that many arguments
 [*+, charAt (I)C
 ./ V exists
 12 V isDirectory
 456 list (Ljava/io/File;ZZZ)V8 Invalid path to a directory path check Ljava/io/File;
 =>? 	listFiles ()[Ljava/io/File;
 AB V isHidden
 DE " getName	 *GH . errJ Error: 
 �L �M -(Ljava/lang/Object;)Ljava/lang/StringBuilder;O  is not a Directory! dir recur Z size hidden filelist [Ljava/io/File; index file nameV 
SourceFile client.java !                          	      
                       	      �  	  |� Y'� L+� Y� � � #+%� '� )/� 1� 6Y� 8Y� :� >� AMN,� DN� :� )G� I� L� PY-� R:� S� ���� W� [:� ]6� [:6� � W� [S����a� c� g� c� �h� c� +� j��_l� c� +� n��Lp� c� +� r��9t� c� +� v��&x� c� +� z��|� c� +� ~�� �� c� +� ������ c� +� ������ c� +� ������ c� +� ����� )�� I���  > C F �  �   v         "  *  <  >  C  H  P  T " ^ # i $ s % z & � ' � ) � + � , � - � / � 0 1% 28 4K 5^ 6q 7y  �   f 
  | �    p � �  <= � �  >; �   H  � �  ^ � �  s �   z � �   � � �   �  �   �   _ � " � #  �  6 [  ��  P�  	 �  6 [ P [ �  �  �   �            �     B*� �*� �*� �**� �� �� ��N6� *� �� �Y-*� �� �S�*� ����    �   & 	   :  ; 	 <  =  >  ? " @ 5 ? A B �   4    B � �     B �     B �    & �    " �   �    � "   [     i     �     Ȳ )�� I� 6Y� 8Y� :� >� AM*,� D� �� N� )G� I� L� )�� I*,� D� �� N� )¶ I� L� )� �YƷ �*� �� �Ͷ �*� �� ɶ ϶ I*� �N**� �� �� �6� 4*� �� �Y�*� �� �S*� �2-� Ҷ ֚ � )۶ I��*� ���ɱ   " % � : B E �  �   V    F  G  I " J & K . L 2 N : P B Q F R N S R U w X | Y � Z � [ � \ � ] � ^ � Z � a �   H    � � �     � �    � � �  &  � �  F  � �  | L �   � = �   �   $ � %   � 6  �R �� : [-   m          �*� �� [M>� (,� �Y*� #� ߷ �%� �a� �� � �S�*� ����>� G*� �2� �Y,2� ߷ �� �+2� ɶ ϻ �Y*� '� ߷ �%� �+2� ɶ ϶ �W�*� �����    �       e  f : g ? h � g � j �   4    � � �     � �    � �   
 0 �   < O �   �    �  �$� C   q      �     i+�� �=� W*� �2� �Y*� #� ߷ �%� �+2� ɶ ϻ �Y*� '� ߷ �%� �+2� ɶ �� �>� � )�� I�*� �����    �       m  n  o P p ] n h r �   *    i � �     i �   	 _ �   P  �   �    � � P   u     h     Բ )�� I� )�� I� )�� I� )�� I� )�� I� )� I� )� I� )� I� )� I� )	� I� )� I� )� I� )� I� )� I� )� I� )� I� )� I� )� I� )� I� )� I� )� I� )!� I� )#� I� )%� I�    �   f    u  v  w  x   y ( z 1 { : | C } L ~ U  ^ � g � p � y � � � � � � � � � � � � � � � � � � � � � �       � � �     � �     y          x+�� *� #M� C+�� � )'� I�+2�)/� 
+2M�  � �Y*� #� ߷ �%� �+2� ɶ �M� Y,� N-�-� -�0� -�3� � )7� I�    �   & 	   �  �  � 0 � M � V � d � n � w � �   >    x � �     x �   
 9   - 9   M +9   V ":;  �    �  [�      }      ?     � )*� #� I�    �       � �        � �      �     �      �     r+�� �+�� � )'� I�+2�)/� 
+2M�  � �Y*� #� ߷ �%� �+2� ɶ �M� Y,� N-�-� -�0� *-� � #� � )7� I�    �   "    �  �  � ) � F � O � h � q � �   4    r � �     r �   & 9   F ,9   O #:;  �    �  [� !    �      5      �    �       � �        � �      �     �      5      �    �       � �        � �      �     �      ?     � )*� '� I�    �       � �        � �      �   
56    P     �*� r*�-� k*�0� d*�<:6� M2:� �@� 7�C:�0�  � )� �Y� ߷ �/� � ϶ I� � )� I������  �F� �YI� �*�KN� ɶ ϶ I�    �   2    �  �  �  � % � 1 � 8 � ] � e � p � s � � � �   R    �P;     �QR    �SR    �TR   XUV   UW   % @X;  8 -Y   �    � Z�  � + [� � 
 [   \