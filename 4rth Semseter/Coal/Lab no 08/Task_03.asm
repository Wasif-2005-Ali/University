INCLUDE Irvine32.inc

.code
main PROC

         mov  ax, 7FF0h
         call DumpRegs

         add  ax, 9000h
         call DumpRegs

         sub  ax, 1000h
         call DumpRegs

         exit

main ENDP
END main