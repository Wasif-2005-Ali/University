INCLUDE Irvine32.inc

.code
main PROC
             mov  eax, 1
             mov  ebx, 0
             mov  ecx, 7

    FibLoop:
             call DumpRegs
             mov  edx, eax
             add  eax, ebx
             mov  ebx, edx

             loop FibLoop

             exit
main ENDP
END main