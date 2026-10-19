INCLUDE Irvine32.inc

.data
         myDword DWORD 12345678h

.code
main PROC

         mov  al, BYTE PTR myDword
         mov  bl, BYTE PTR myDword+1
         mov  cl, BYTE PTR myDword+2
         mov  dl, BYTE PTR myDword+3

         call DumpRegs

         exit
main ENDP
END main