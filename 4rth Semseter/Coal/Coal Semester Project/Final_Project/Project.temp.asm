INCLUDE c:\Users\Wasif Ali\.vscode\extensions\istareatscreens.masm-runner-0.9.1\native\irvine\Irvine32.inc

; =================================================================
; SEMESTER PROJECT: REAL-TIME DIGITAL CLOCK & STOPWATCH
; Author: Wasif Ali
; Department: Computer Science, Air University
; Description: 7-Segment UI, Live Date, and Interactive Stopwatch
; =================================================================

.data
         sysTime     SYSTEMTIME <>

         ; --- VISUAL SETTINGS ---
         blockChar   BYTE '#'                                                       ; Universal solid block character
         baseX       BYTE ?
         baseY       BYTE 8                                                         ; Moved down for header
         colorVal    BYTE 9                                                         ; Starting color (Light Blue)
         lastSecond  BYTE 255                                                       ; Used to prevent strobe-light colors

         ; --- STATE MACHINE VARIABLES ---
         currentMode BYTE 0                                                         ; 0 = Clock Mode, 1 = Stopwatch Mode

         ; --- STOPWATCH VARIABLES ---
         swState     BYTE 0                                                         ; 0 = Stopped, 1 = Running
         timeStart   DWORD 0                                                        ; Timestamp when started
         timeElapsed DWORD 0                                                        ; Total accumulated time
         swMins      DWORD 0
         swSecs      DWORD 0
         swCentis    DWORD 0

         ; --- UI STRINGS ---
         headerStr   BYTE "=================================================", 0
         titleStr    BYTE "   SEMESTER PROJECT: MULTI-FUNCTION TIME UI    ", 0
         authorStr   BYTE "   Developed by: Wasif Ali - Air University    ", 0
         slashStr    BYTE " / ", 0

         ; Footer Messages
         msgClock    BYTE " MODE: CLOCK      [Press 'S' for Stopwatch]      ", 0
         msgStopW    BYTE " MODE: STOPWATCH  [Press 'C' for Clock]          ", 0
         msgSWKeys   BYTE " [SPACE] = Start/Stop      [R] = Reset           ", 0
         clearLine   BYTE "                                                 ", 0

.code
main PROC
                       call   Clrscr

                       ; =========================================================
                       ; 1. DRAW STATIC HEADER ONCE
                       ; =========================================================
                       mov    eax, white + (black * 16)
                       call   SetTextColor

                       mov    dl, 15
                       mov    dh, 1
                       call   Gotoxy
                       mov    edx, OFFSET headerStr
                       call   WriteString

                       mov    dl, 15
                       mov    dh, 2
                       call   Gotoxy
                       mov    edx, OFFSET titleStr
                       call   WriteString

                       mov    dl, 15
                       mov    dh, 3
                       call   Gotoxy
                       mov    edx, OFFSET authorStr
                       call   WriteString

                       mov    dl, 15
                       mov    dh, 4
                       call   Gotoxy
                       mov    edx, OFFSET headerStr
                       call   WriteString

                       ; Initialize the time
                       INVOKE GetLocalTime, ADDR sysTime

    ; =========================================================
    ; MAIN APPLICATION LOOP
    ; =========================================================
    MainLoop:
                       ; Check for keyboard input without pausing
                       call   HandleInput

                       ; Fetch current time for background logic
                       INVOKE GetLocalTime, ADDR sysTime

                       ; Only cycle color once per real second (prevents strobing)
                       movzx  eax, sysTime.wSecond
                       cmp    al, lastSecond
                       je     DrawActiveMode
                       mov    lastSecond, al
                       inc    colorVal
                       cmp    colorVal, 15
                       jbe    ApplyColor
                       mov    colorVal, 9                                                                                                    ; Reset to light blue if it passes white
    ApplyColor:
                       movzx  eax, colorVal
                       call   SetTextColor

    DrawActiveMode:
                       ; Route to correct mode
                       cmp    currentMode, 0
                       je     ModeClock
                       jmp    ModeStopwatch

    ; ---------------------------------------------------------
    ; MODE 0: REAL-TIME CLOCK + DATE
    ; ---------------------------------------------------------
    ModeClock:
                       ; Draw Hours
                       movzx  eax, sysTime.wHour
                       mov    bl, 10
                       div    bl
                       mov    dl, 10
                       push   eax | movzx eax, al | call DrawHugeDigit | pop eax
                       mov    dl, 20
                       movzx  eax, ah | call DrawHugeDigit

                       ; Draw Colon 1
                       mov    dl, 30 | call DrawColon

                       ; Draw Minutes
                       movzx  eax, sysTime.wMinute
                       mov    bl, 10
                       div    bl
                       mov    dl, 35
                       push   eax | movzx eax, al | call DrawHugeDigit | pop eax
                       mov    dl, 45
                       movzx  eax, ah | call DrawHugeDigit

                       ; Draw Colon 2
                       mov    dl, 55 | call DrawColon

                       ; Draw Seconds
                       movzx  eax, sysTime.wSecond
                       mov    bl, 10
                       div    bl
                       mov    dl, 60
                       push   eax | movzx eax, al | call DrawHugeDigit | pop eax
                       mov    dl, 70
                       movzx  eax, ah | call DrawHugeDigit

                       ; --- DRAW REAL DATE (DD / MM / YYYY) ---
                       mov    eax, white + (black * 16)
                       call   SetTextColor
                       mov    dl, 33 | mov dh, 18 | call Gotoxy
                       movzx  eax, sysTime.wDay | call WriteZeroPadded
                       mov    edx, OFFSET slashStr | call WriteString
                       movzx  eax, sysTime.wMonth | call WriteZeroPadded
                       call   WriteString
                       movzx  eax, sysTime.wYear | call WriteDec

                       ; --- DRAW CLOCK FOOTER ---
                       mov    dl, 15 | mov dh, 21 | call Gotoxy
                       mov    edx, OFFSET msgClock | call WriteString
                       mov    dl, 15 | mov dh, 22 | call Gotoxy
                       mov    edx, OFFSET clearLine | call WriteString

                       jmp    LoopDelay

    ; ---------------------------------------------------------
    ; MODE 1: STOPWATCH (MM:SS:CC)
    ; ---------------------------------------------------------
    ModeStopwatch:
                       ; Calculate Elapsed Milliseconds
                       cmp    swState, 1
                       jne    CalcStopped
                       call   GetMSeconds
                       sub    eax, timeStart
                       add    eax, timeElapsed
                       jmp    DoMath
    CalcStopped:
                       mov    eax, timeElapsed

    DoMath:
                       ; EAX = Total Milliseconds. Now we split it into MM, SS, Centiseconds
                       mov    edx, 0
                       mov    ebx, 60000
                       div    ebx
                       mov    swMins, eax                                                                                                    ; Store Minutes

                       mov    eax, edx
                       mov    edx, 0
                       mov    ebx, 1000
                       div    ebx
                       mov    swSecs, eax                                                                                                    ; Store Seconds

                       mov    eax, edx
                       mov    edx, 0
                       mov    ebx, 10
                       div    ebx
                       mov    swCentis, eax                                                                                                  ; Store Centiseconds (0-99)

                       ; Restore Color for Stopwatch Digits
                       movzx  eax, colorVal
                       call   SetTextColor

                       ; Draw Stopwatch Minutes
                       mov    eax, swMins
                       mov    bl, 10
                       div    bl
                       mov    dl, 10
                       push   eax | movzx eax, al | call DrawHugeDigit | pop eax
                       mov    dl, 20
                       movzx  eax, ah | call DrawHugeDigit

                       mov    dl, 30 | call DrawColon

                       ; Draw Stopwatch Seconds
                       mov    eax, swSecs
                       mov    bl, 10
                       div    bl
                       mov    dl, 35
                       push   eax | movzx eax, al | call DrawHugeDigit | pop eax
                       mov    dl, 45
                       movzx  eax, ah | call DrawHugeDigit

                       mov    dl, 55 | call DrawColon

                       ; Draw Stopwatch Centiseconds
                       mov    eax, swCentis
                       mov    bl, 10
                       div    bl
                       mov    dl, 60
                       push   eax | movzx eax, al | call DrawHugeDigit | pop eax
                       mov    dl, 70
                       movzx  eax, ah | call DrawHugeDigit

                       ; --- DRAW STOPWATCH FOOTER ---
                       mov    eax, white + (black * 16)
                       call   SetTextColor

                       mov    dl, 15 | mov dh, 18 | call Gotoxy
                       mov    edx, OFFSET clearLine | call WriteString                                                                       ; Clear date line

                       mov    dl, 15 | mov dh, 21 | call Gotoxy
                       mov    edx, OFFSET msgStopW | call WriteString
                       mov    dl, 15 | mov dh, 22 | call Gotoxy
                       mov    edx, OFFSET msgSWKeys | call WriteString

    LoopDelay:
                       ; 50ms delay for 20 FPS (Fast enough for stopwatch responsiveness)
                       mov    eax, 50
                       call   Delay
                       jmp    MainLoop

    QuitProg:
                       mov    eax, lightGray + (black * 16)
                       call   SetTextColor
                       call   Clrscr
                       exit
main ENDP

; =================================================================
; INPUT HANDLER (State Machine Controller)
; =================================================================
HandleInput PROC
                       call   ReadKey
                       jz     NoInput

                       cmp    al, 27                                                                                                         ; ESC Key -> Exit
                       je     QuitProg

                       cmp    al, 'c'                                                                                                        ; 'C' or 'c' -> Clock Mode
                       je     SwitchToClock
                       cmp    al, 'C'
                       je     SwitchToClock

                       cmp    al, 's'                                                                                                        ; 'S' or 's' -> Stopwatch Mode
                       je     SwitchToStopwatch
                       cmp    al, 'S'
                       je     SwitchToStopwatch

                       cmp    al, ' '                                                                                                        ; SPACE -> Start/Stop Stopwatch
                       je     ToggleStopwatch

                       cmp    al, 'r'                                                                                                        ; 'R' or 'r' -> Reset Stopwatch
                       je     ResetStopwatch
                       cmp    al, 'R'
                       je     ResetStopwatch
                       jmp    NoInput

    SwitchToClock:
                       mov    currentMode, 0
                       ret
    SwitchToStopwatch:
                       mov    currentMode, 1
                       ret
    ToggleStopwatch:
                       cmp    currentMode, 1                                                                                                 ; Only toggle if currently in Stopwatch mode
                       jne    NoInput
                       xor    swState, 1                                                                                                     ; Flip state between 0 and 1
                       cmp    swState, 1
                       jne    Pausing
                       ; Starting: Record current time
                       call   GetMSeconds
                       mov    timeStart, eax
                       ret
    Pausing:
                       ; Pausing: Calculate difference and add to elapsed
                       call   GetMSeconds
                       sub    eax, timeStart
                       add    timeElapsed, eax
                       ret
    ResetStopwatch:
                       cmp    currentMode, 1
                       jne    NoInput
                       mov    swState, 0
                       mov    timeElapsed, 0
                       mov    timeStart, 0
                       ret
    NoInput:
                       ret
HandleInput ENDP

; =================================================================
; HELPER: ZERO-PADDED NUMBER PRINTING
; =================================================================
WriteZeroPadded PROC
                       cmp    eax, 10
                       jae    PrintNumber
                       push   eax
                       mov    al, '0'
                       call   WriteChar
                       pop    eax
    PrintNumber:
                       call   WriteDec
                       ret
WriteZeroPadded ENDP

; =================================================================
; 7-SEGMENT RENDERING ENGINE (Using ASCII Blocks)
; =================================================================
DrawHugeDigit PROC
                       mov    baseX, dl

                       push   eax
                       call   ClearDigitArea
                       pop    eax

                       cmp    eax, 0 | jne chk1 | call SegA | call SegB | call SegC | call SegD | call SegE | call SegF | ret
    chk1:              cmp    eax, 1 | jne chk2 | call SegB | call SegC | ret
    chk2:              cmp    eax, 2 | jne chk3 | call SegA | call SegB | call SegG | call SegE | call SegD | ret
    chk3:              cmp    eax, 3 | jne chk4 | call SegA | call SegB | call SegC | call SegD | call SegG | ret
    chk4:              cmp    eax, 4 | jne chk5 | call SegF | call SegG | call SegB | call SegC | ret
    chk5:              cmp    eax, 5 | jne chk6 | call SegA | call SegF | call SegG | call SegC | call SegD | ret
    chk6:              cmp    eax, 6 | jne chk7 | call SegA | call SegF | call SegE | call SegD | call SegC | call SegG | ret
    chk7:              cmp    eax, 7 | jne chk8 | call SegA | call SegB | call SegC | ret
    chk8:              cmp    eax, 8 | jne chk9 | call SegA | call SegB | call SegC | call SegD | call SegE | call SegF | call SegG | ret
    chk9:              call   SegA | call SegB | call SegC | call SegD | call SegF | call SegG | ret
DrawHugeDigit ENDP

; --- Individual Segments ---
SegA PROC
                       mov    dl, baseX | add dl, 1 | mov dh, baseY | call Gotoxy
                       mov    al, blockChar | call WriteChar | call WriteChar | call WriteChar | call WriteChar | ret
SegA ENDP

SegB PROC
                       mov    dl, baseX | add dl, 5 | mov dh, baseY | add dh, 1 | call Gotoxy | mov al, blockChar | call WriteChar
                       inc    dh | call Gotoxy | call WriteChar | ret
SegB ENDP

SegC PROC
                       mov    dl, baseX | add dl, 5 | mov dh, baseY | add dh, 4 | call Gotoxy | mov al, blockChar | call WriteChar
                       inc    dh | call Gotoxy | call WriteChar | ret
SegC ENDP

SegD PROC
                       mov    dl, baseX | add dl, 1 | mov dh, baseY | add dh, 6 | call Gotoxy
                       mov    al, blockChar | call WriteChar | call WriteChar | call WriteChar | call WriteChar | ret
SegD ENDP

SegE PROC
                       mov    dl, baseX | mov dh, baseY | add dh, 4 | call Gotoxy | mov al, blockChar | call WriteChar
                       inc    dh | call Gotoxy | call WriteChar | ret
SegE ENDP

SegF PROC
                       mov    dl, baseX | mov dh, baseY | add dh, 1 | call Gotoxy | mov al, blockChar | call WriteChar
                       inc    dh | call Gotoxy | call WriteChar | ret
SegF ENDP

SegG PROC
                       mov    dl, baseX | add dl, 1 | mov dh, baseY | add dh, 3 | call Gotoxy
                       mov    al, blockChar | call WriteChar | call WriteChar | call WriteChar | call WriteChar | ret
SegG ENDP

DrawColon PROC
                       mov    dh, baseY | add dh, 2 | call Gotoxy | mov al, blockChar | call WriteChar
                       mov    dh, baseY | add dh, 4 | call Gotoxy | mov al, blockChar | call WriteChar
                       ret
DrawColon ENDP

ClearDigitArea PROC
                       push   eax
                       push   ecx
                       mov    dh, baseY
                       mov    ecx, 7
    clrLoop:
                       mov    dl, baseX | call Gotoxy | mov al, ' '
                       call   WriteChar | call WriteChar | call WriteChar | call WriteChar | call WriteChar | call WriteChar
                       inc    dh | loop clrLoop
                       pop    ecx
                       pop    eax
                       ret
ClearDigitArea ENDP

END main