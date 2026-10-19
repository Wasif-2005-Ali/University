INCLUDE Irvine32.inc

.data
         Uarray WORD 1000h,2000h,3000h,4000h
         Sarray SWORD -1,-2,-3,-4

.code
main PROC

         movzx eax, Uarray
         movzx ebx, Uarray+2
         movzx ecx, Uarray+4
         movzx edx, Uarray+6

         call  DumpRegs

         movsx eax, Sarray
         movsx ebx, Sarray+2
         movsx ecx, Sarray+4
         movsx edx, Sarray+6

         call  DumpRegs

         exit
main ENDP

END main