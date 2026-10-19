INCLUDE Irvine32.inc

.data
         s1 BYTE 1,2,3
         s2 WORD 1,2,3,4
         s3 DWORD 1,2
         s4 BYTE 6 DUP(0)
         s5 WORD 2 DUP(5)

.code
main PROC

         mov  eax, SIZEOF s1
         call WriteInt
         call Crlf

         mov  eax, SIZEOF s2
         call WriteInt
         call Crlf

         mov  eax, SIZEOF s3
         call WriteInt
         call Crlf

         mov  eax, SIZEOF s4
         call WriteInt
         call Crlf

         mov  eax, SIZEOF s5
         call WriteInt
         call Crlf

         exit
main ENDP
END main