INCLUDE Irvine32.inc

.data
         arrayD DWORD 10000h,20000h,30000h

.code
main PROC

         mov  esi, OFFSET arrayD

         mov  eax, [esi]

         add  esi, 4
         add  eax, [esi]

         add  esi, 4
         add  eax, [esi]

         call DumpRegs

         exit
main ENDP
END main