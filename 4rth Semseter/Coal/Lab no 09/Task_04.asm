INCLUDE Irvine32.inc

.data
         bVal BYTE 10h
         wVal WORD 1234h
         dVal DWORD 12345678h
         qVal QWORD 1122334455667788h

.code
main PROC

         mov  eax, TYPE bVal
         call WriteInt
         call Crlf

         mov  eax, TYPE wVal
         call WriteInt
         call Crlf

         mov  eax, TYPE dVal
         call WriteInt
         call Crlf

         mov  eax, TYPE qVal
         call WriteInt
         call Crlf

         exit
main ENDP
END main