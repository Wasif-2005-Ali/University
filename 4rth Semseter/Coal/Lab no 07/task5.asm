    INCLUDE Irvine32.inc

.code
main PROC

         mov  al, 0FFh
         inc  al          ; AL = 00h → ZF=1, OF=0

         call DumpRegs    ; show registers & flags

         mov  bl, 00h
         dec  bl          ; BL = FFh → SF=1

         call DumpRegs

         exit
main ENDP
END main