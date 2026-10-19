INCLUDE Irvine32.inc

.data
         bval  BYTE ?
         wval  WORD ?
         dval  DWORD ?
         dval2 DWORD ?

.code
main PROC

         mov  eax, OFFSET bval
         call WriteInt
         call Crlf

         mov  eax, OFFSET wval
         call WriteInt
         call Crlf

         mov  eax, OFFSET dval
         call WriteInt
         call Crlf

         mov  eax, OFFSET dval2
         call WriteInt
         call Crlf

         exit
main ENDP
END main