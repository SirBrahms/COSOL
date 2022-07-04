
namespace Cosol
{
    class Program
    {
        static void Main(string[] Args)
        {
            if (Args.Length == 0)
            {
                // No Arguments supplied --> Run Console
                Console.WriteLine("Ok");
            }
            else
            {
                // Open File and interpret
                // Read File supplied in Args[0]
                StreamReader Reader = new StreamReader(Args[0]);
            }
        }
    }

    # region Tokens
    
    class Token
    {
        protected object? _Value;
        protected Action? _OnCall;
        public bool Callable = false; // Bool to determine wether this Token can be called

        // Property for the Value
        public virtual object Value 
        {
            get
            {
                if (_Value != null)
                    return _Value;
                else
                    throw new NullReferenceException("No Value was set");
            }
            set
            {
                {
                    _Value = (object) value;
                }
            }
        }

        // Property for the Action
        public virtual Action OnCall
        {
            get
            {
                if (_OnCall != null && Callable)
                    return _OnCall;
                else if (!Callable)
                    throw new CallException("Action was not callable");
                else
                    throw new NullReferenceException("No Action was set");
            }
            set
            {
                {
                    _OnCall = (Action) value;
                }
            }
        }

        public Token(object Val)
        {
            this.Value = Val;
            this.Callable = false;
        }

        public Token(object Val, Action OnCall)
        {
            this.Value = Val;
            this.OnCall = OnCall;
            this.Callable = true;
        }

        public Token(object Val, Action OnCall, bool Callable)
        {
            this.Value = Val;
            this.OnCall = OnCall;
            this.Callable = Callable;
        }

        public Token(object Val, bool Callable)
        {
            this.Value = Val;
            this.Callable = Callable;
        }
    }

    # endregion

    # region Exceptions

    class CallException : Exception
    {
        public CallException() {}
        public CallException(string Message) : base(Message) {}
        public CallException(string Message, Exception Inner) : base(Message, Inner) {}
    }

    # endregion
}

