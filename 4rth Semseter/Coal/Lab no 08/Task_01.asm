INCLUDE Irvine32.inc

.data
         Val1 BYTE 25h
         Val2 BYTE 70h

.code
main PROC

         mov   al, Val1
         xchg  al, Val2
         mov   Val1, al


         movzx eax, Val1
         movzx ebx, Val2

         call  DumpRegs

         exit

main ENDP
END main