INCLUDE Irvine32.inc

.data
         array DWORD 10h, 20h, 30h, 40h

.code
main PROC
         mov  esi, OFFSET array      ; Starting address
         mov  ecx, LENGTHOF array    ; Number of units
         mov  ebx, TYPE array        ; Unit size (4 bytes)
         call DumpMem
         exit
main ENDP
END main