INCLUDE Irvine32.inc

.data
         myWord WORD 1234h

.code
main PROC

         mov  ax, myWord

         mov  BYTE PTR myWord, 0ABh

         call DumpRegs

         exit
main ENDP
END main