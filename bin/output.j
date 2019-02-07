.class public Output
.super java/lang/Object

.method public <init>()V
 aload_0
 invokenonvirtual java/lang/Object/<init>()V
 return
.end method

.method public static print(I)V
 .limit stack 2
 getstatic java/lang/System/out Ljava/io/PrintStream;
 iload_0 
 invokestatic java/lang/Integer/toString(I)Ljava/lang/String;
 invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V
 return
.end method

.method public static read()I
 .limit stack 3
 new java/util/Scanner
 dup
 getstatic java/lang/System/in Ljava/io/InputStream;
 invokespecial java/util/Scanner/<init>(Ljava/io/InputStream;)V
 invokevirtual java/util/Scanner/next()Ljava/lang/String;
 invokestatic java/lang/Integer.parseInt(Ljava/lang/String;)I
 ireturn
.end method

.method public static run()V
 .limit stack 1024
 .limit locals 256
 ldc 1
 istore 0
L1:
 ldc 3
 istore 1
L2:
 ldc 3
 istore 2
L3:
 ldc 4
 istore 3
L4:
 iload 0
 ldc 2
 if_icmpgt L8
 iload 2
 ldc 4
 if_icmpne L8
 goto L7
L8:
 iload 3
 ldc 3
 if_icmpeq L9
 iload 1
 ldc 3
 if_icmpeq L9
 goto L7
L9:
 goto L6
L7:
 ldc 1
 invokestatic Output/print(I)V
 goto L5
L6:
 ldc 0
 invokestatic Output/print(I)V
L5:
L0:
 return
.end method

.method public static main([Ljava/lang/String;)V
 invokestatic Output/run()V
 return
.end method

