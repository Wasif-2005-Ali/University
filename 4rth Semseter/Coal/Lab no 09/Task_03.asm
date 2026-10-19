INCLUDE Irvine32.inc

.data
         arr BYTE 11h,22h,33h,44h

.code
main PROC

         mov  ax, WORD PTR arr
         mov  bx, WORD PTR arr+1

         call DumpRegs

         exit
main ENDP
END main
