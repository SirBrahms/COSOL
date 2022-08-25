namespace COSOL
{
    class Program
    {
        // char[] -> Form in the char[]
        // string -> Concatonated Form
        public static Dictionary<string, char[]> Patterns = new Dictionary<string, char[]>()
        {
            {"->", new char[] {'-', '>'}}, // LARROW
            {"<-", new char[] {'<', '-'}}, // RARROW
        };

        static void Main(string[] Args)
        {
            if (Args.Length == 0) 
            {
                try 
                {
                    while (true) 
                    {
                        Console.Write("> ");
                        string? Instruction;
                        if ((Instruction = Console.ReadLine()) == null)
                            return;
                        Interpret(Instruction);
                    }
                }
                catch (Exception)
                {
                    Console.WriteLine(">?");
                    Environment.Exit(0);
                }
            }
            else 
            {
                // Read file supplied in args[0]
                try 
                {
                    StreamReader Reader = new StreamReader(Args[0]);
                    string FullString;
                    FullString = Reader.ReadToEnd();

                    FullString.Replace("/n", "");
                    Interpret(FullString);
                } 
                catch (Exception) 
                {
                    Console.WriteLine("File not found!");
                    Environment.Exit(1);
                }
                
            }
        }

        static void Interpret(string FullString)
        {
            char[] Chars = FullString.ToCharArray();
            BundleChars(Chars);
        }

        static string[] BundleChars(char[] Chars)
        {
            List<string> Result = new List<string>();
            
            
            for (int i = 0; i < Chars.Length; i++)
            {
                bool AddChar = true;

                foreach (KeyValuePair<string, char[]> KVP in Patterns)
                {
                    string Pattern = KVP.Key;

                    if (Chars.Skip(i).Take(Patterns[Pattern].Length).SequenceEqual(Patterns[Pattern])) // Find the Sequence (Pattern)
                    {
                        Result.Add(Pattern); // Add the Pattern to the Result

                        AddChar = false; // Skip the next character
                    }
                }

                if (AddChar)
                {
                    Result.Add(Chars[i].ToString()); // If the Sequence wasn't found, just add the current char to Result
                }
                else
                {
                    i++; // If the sequence was found, skip the next char
                    AddChar = true; // Allow for more chars to be added
                }
                
            }

            return Result.ToArray();
        }
    }
}

