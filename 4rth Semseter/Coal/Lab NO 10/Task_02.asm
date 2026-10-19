INCLUDE Irvine32.inc

.data
         Uarray WORD 1000h,2000h,3000h,4000h
         Sarray SWORD -1,-2,-3,-4

.code
main PROC

         mov   esi, OFFSET Uarray
         mov   ax, [esi]
         movzx eax, ax
         mov   bx, [esi+2]
         movzx ebx, bx
         mov   cx, [esi+4]
         movzx ecx, cx
         mov   dx, [esi+6]
         movzx edx, dx
         call  DumpRegs



         movsx eax, Sarray
         movsx ebx, Sarray+2
         movsx ecx, Sarray+4
         movsx edx, Sarray+6

         call  DumpRegs

         exit

main ENDP
END main