INCLUDE Irvine32.inc

.data
    askTitle DB "Question", 0
    askMsg   DB "Do you want to continue?", 0
    yesMsg   DB "you selected yes", 0

.code
main PROC
         mov  ebx, OFFSET askTitle
         mov  edx, OFFSET askMsg
         call MsgBoxAsk

         cmp  eax, IDYES
         jne  quit

         mov  ebx, OFFSET askTitle
         mov  edx, OFFSET yesMsg
         call MsgBox

    quit:
         exit
main ENDP
END main